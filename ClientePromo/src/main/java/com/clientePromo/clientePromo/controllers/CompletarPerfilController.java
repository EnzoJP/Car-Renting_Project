package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.DTO.*;
import com.clientePromo.clientePromo.auth.AuthService;
import com.clientePromo.clientePromo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CompletarPerfilController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ContactoService contactoService;
    @Autowired
    private NacionalidadService nacionalidadService;
    @Autowired
    private LocalidadService localidadService;

    @GetMapping("/completar-perfil")
    public String mostrarFormulario(Model model) {
        UsuarioDTO usuario = authService.getUsuarioAutenticado();
        if (usuario == null) {
            return "redirect:/login";
        }
        // Si ya completó sus datos, va directo al dashboard
        if (usuario.getFechaNacimiento() != null && usuario.getNumeroDocumento() != null) {
            return "redirect:/cliente/dashboard";
        }
        try {
            ClienteDTO cliente = clienteService.findByUsuarioId(usuario.getId());
            model.addAttribute("cliente", cliente);
            model.addAttribute("usuario", usuario);
            model.addAttribute("nacionalidades", nacionalidadService.listarActivos());
            model.addAttribute("localidades", localidadService.listarActivos());
            model.addAttribute("titulo", "Completar Perfil");
            return "view/completar-perfil";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo cargar la información para completar el perfil.");
            return "redirect:/";
        }
    }

    @PostMapping("/completar-perfil")
    public String completarPerfil(@RequestParam String fechaNacimiento, @RequestParam String tipoDocumento, @RequestParam String numeroDocumento, @RequestParam String telefono,@RequestParam(required = false) Long nacionalidadId, @RequestParam(required = false) String direccionEstadia, @RequestParam(required = false) String calle, @RequestParam(required = false) String numeracion, @RequestParam(required = false) String barrio, @RequestParam(required = false) String manzanaPiso, @RequestParam(required = false) String casaDepartamento, @RequestParam(required = false) Long localidadId, RedirectAttributes redirectAttributes, Model model
    ) {
        UsuarioDTO usuario = authService.getUsuarioAutenticado();
        if (usuario == null) {
            return "redirect:/login";
        }
        try {
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimiento);
            // Crear DTO con TODOS los campos necesarios
            ContactoDTO contacto = new ContactoDTO();
            contacto.setTipoContacto("PERSONAL");
            contacto.setObservacion(telefono);
            List<ContactoDTO> contactos = new ArrayList<>();
            contactos.add(contacto);

            // Objeto Dirección
            DireccionDTO direccion = new DireccionDTO();
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setBarrio(barrio);
            direccion.setManzanaPiso(manzanaPiso);
            direccion.setCasaDepartamento(casaDepartamento);
            if (localidadId != null) {
                LocalidadDTO localidad = new LocalidadDTO();
                localidad.setId(localidadId);
                direccion.setLocalidad(localidad);
            }

            // --- 2. Actualizar la entidad USUARIO (Persona 1) ---
            UsuarioDTO usuarioParaActualizar = new UsuarioDTO();
            usuarioParaActualizar.setId(usuario.getId());
            usuarioParaActualizar.setNombreUsuario(usuario.getNombreUsuario());
            usuarioParaActualizar.setNombre(usuario.getNombre());
            usuarioParaActualizar.setApellido(usuario.getApellido());
            usuarioParaActualizar.setRol(usuario.getRol());
            usuarioParaActualizar.setProvider(usuario.getProvider());
            usuarioParaActualizar.setProviderId(usuario.getProviderId());
            usuarioParaActualizar.setPictureUrl(usuario.getPictureUrl());

            // Añadir datos del formulario al Usuario
            usuarioParaActualizar.setFechaNacimiento(fecha);
            usuarioParaActualizar.setTipoDocumento(tipoDocumento);
            usuarioParaActualizar.setNumeroDocumento(numeroDocumento);
            usuarioParaActualizar.setContactos(contactos);
            usuarioParaActualizar.setDireccion(direccion);
            usuarioParaActualizar.setPerfilCompleto(true);

            usuarioService.modificar(usuario.getId(), usuarioParaActualizar);

            // --- 3. Actualizar la entidad CLIENTE (Persona 2) ---
            actualizarCliente(usuario.getId(), fecha, tipoDocumento, numeroDocumento, contacto, direccion, nacionalidadId, direccionEstadia);

            // --- 4. Recargar sesión y redirigir ---
            authService.setToken(authService.getToken());

            redirectAttributes.addFlashAttribute("mensaje", "¡Perfil completado exitosamente! 😎");
            return "redirect:/cliente/dashboard";

        } catch (Exception e) {
            System.err.println("ERROR al completar perfil:");
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("usuario", usuario); // Repoblar datos del header

            // Repoblar datos de los <select> en caso de error
            model.addAttribute("nacionalidades", nacionalidadService.listarActivos());
            model.addAttribute("localidades", localidadService.listarActivos());
            // Repoblar el cliente para los campos del formulario
            model.addAttribute("cliente", clienteService.findByUsuarioId(usuario.getId()));

            return "view/completar-perfil";
        }
    }

    // Crea o actualiza un contacto telefónico dentro de la lista
    private ContactoDTO obtenerOActualizarContacto(UsuarioDTO usuario, String telefono) {
        List<ContactoDTO> contactos = usuario.getContactos();
        if (contactos == null) contactos = new ArrayList<>();
        ContactoDTO contacto = contactos.stream()
                .filter(c -> "TELEFONO".equalsIgnoreCase(c.getTipoContacto()))
                .findFirst()
                .orElse(new ContactoDTO());

        contacto.setTipoContacto("TELEFONO");
        contacto.setObservacion(telefono);
        if (contacto.getId() == null) {
            return contactoService.alta(contacto);
        } else {
            contactoService.modificar(contacto.getId(), contacto);
            return contacto;
        }
    }

    private void actualizarCliente(Long usuarioId, Date fecha, String tipoDoc, String nroDoc, ContactoDTO contacto, DireccionDTO direccion, Long nacionalidadId, String direccionEstadia) {
        try {
            ClienteDTO cliente = clienteService.findByUsuarioId(usuarioId);
            if (cliente != null) {
                // Datos Personales
                cliente.setFechaNacimiento(fecha);
                cliente.setTipoDocumento(tipoDoc);
                cliente.setNumeroDocumento(nroDoc);

                // Datos de Dirección
                cliente.setDireccion(direccion); // Asignar la dirección completa

                // Datos de Contacto
                if (cliente.getContactos() == null) {
                    cliente.setContactos(new ArrayList<>());
                }
                cliente.getContactos().clear(); // Limpiar por si acaso
                cliente.getContactos().add(contacto);

                // --- NUEVOS CAMPOS para Cliente ---
                cliente.setDireccionEstadia(direccionEstadia);

                if (nacionalidadId != null) {
                    NacionalidadDTO nacionalidad = new NacionalidadDTO();
                    nacionalidad.setId(nacionalidadId);
                    cliente.setNacionalidad(nacionalidad);
                }

                clienteService.modificar(cliente.getId(), cliente);
                System.out.println("Cliente (ID: " + cliente.getId() + ") actualizado correctamente");
            } else {
                System.err.println("No se encontró el cliente para el usuario ID: " + usuarioId);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar cliente (ID Usuario: " + usuarioId + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
}
