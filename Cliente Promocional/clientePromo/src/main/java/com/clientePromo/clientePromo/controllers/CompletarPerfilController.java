package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.DTO.ClienteDTO;
import com.clientePromo.clientePromo.DTO.ContactoDTO;
import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import com.clientePromo.clientePromo.auth.AuthService;
import com.clientePromo.clientePromo.services.ClienteService;
import com.clientePromo.clientePromo.services.ContactoService;
import com.clientePromo.clientePromo.services.UsuarioService;
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

    @GetMapping("/completar-perfil")
    public String mostrarFormulario(Model model) {
        UsuarioDTO usuario = authService.getUsuarioAutenticado();

        if (usuario == null) {
            return "redirect:/login";
        }

        // Si ya completó sus datos, va directo al home
        if (usuario.getFechaNacimiento() != null && usuario.getNumeroDocumento() != null) {
            return "redirect:/cliente/dashboard";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Completar Perfil");
        return "view/completar-perfil";
    }

    @PostMapping("/completar-perfil")
    public String completarPerfil(@RequestParam String fechaNacimiento, @RequestParam String tipoDocumento, @RequestParam String numeroDocumento, @RequestParam String telefono, RedirectAttributes redirectAttributes, Model model
    ) {
        UsuarioDTO usuario = authService.getUsuarioAutenticado();

        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaNacimiento);

            usuario.setFechaNacimiento(fecha);
            usuario.setTipoDocumento(tipoDocumento);
            usuario.setNumeroDocumento(numeroDocumento);

            ContactoDTO contacto = new ContactoDTO();
            contacto.setTipoContacto("PERSONAL"); // Usar enum string
            contacto.setObservacion(telefono);
            // Guardar contacto primero
            ContactoDTO contactoGuardado = contactoService.alta(contacto);
            // Asignar lista de contactos
            List<ContactoDTO> contactos = new ArrayList<>();
            contactos.add(contactoGuardado);
            usuario.setContactos(contactos);

            usuarioService.modificar(usuario.getId(), usuario);
            actualizarCliente(usuario, fecha, tipoDocumento, numeroDocumento, contactoGuardado);
            // Recargar perfil
            authService.setToken(authService.getToken());

            redirectAttributes.addFlashAttribute("mensaje", "¡Perfil completado exitosamente! 😎");
            return "redirect:/cliente/dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar los datos. Verifica los campos e intenta nuevamente.");
            model.addAttribute("usuario", usuario);
            e.printStackTrace();
            return "completar-perfil";
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

    private void actualizarCliente(UsuarioDTO usuario, Date fecha, String tipoDoc, String nroDoc, ContactoDTO contacto) {
        try {
            ClienteDTO cliente = clienteService.findByUsuarioId(usuario.getId());
            if (cliente != null) {
                cliente.setFechaNacimiento(fecha);
                cliente.setTipoDocumento(tipoDoc);
                cliente.setNumeroDocumento(nroDoc);

                List<ContactoDTO> contactos = new ArrayList<>();
                contactos.add(contacto);
                cliente.setContactos(contactos);

                clienteService.modificar(cliente.getId(), cliente);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
