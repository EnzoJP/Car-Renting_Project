package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ClienteDTO;
import com.clienteAdmin.clienteAdmin.DTO.ImagenDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cliente")
public class ClienteController extends BaseController<ClienteDTO, Long> {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private NacionalidadService nacionalidadService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private DireccionService direccionService;

    public ClienteController(ClienteService service) {
        super(service);
        initController(new ClienteDTO(),
                "LIST CLIENTES",
                "EDIT CLIENTES",
                "view/admin/Menu/usuarios/");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        model.addAttribute("nacionalidades", nacionalidadService.listarActivos());
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        model.addAttribute("nacionalidades", nacionalidadService.listarActivos());
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }

    @Override
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("item") ClienteDTO entidad,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes attributes,
                             Model model) {
        try {
            this.model = model;
            preActualziacion();

            if (file != null && !file.isEmpty()) {
                // Creamos una nueva ImagenDTO con los datos del archivo
                ImagenDTO imagen = new ImagenDTO();
                imagen.setContenido(file.getBytes());
                imagen.setMime(file.getContentType());

                entidad.setImagen(imagen);
            }
            // --- Fin Lógica de Imagen ---

            if (entidad.getId() == null) {
                service.alta(entidad);
            } else {
                service.modificar(entidad.getId(), entidad);
            }

            postActualziacion();
            attributes.addFlashAttribute("msgExito", "La acción fue realizada correctamente.");
            return redirectList;

        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());

            try {
                preModificacion();
            } catch (ErrorServiceException ex) {
                model.addAttribute("msgError", "Error de Sistema al recargar el formulario.");
            }
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema: " + e.getMessage());
            try {
                preModificacion();
            } catch (ErrorServiceException ex) {
                model.addAttribute("msgError", "Error de Sistema al recargar el formulario.");
            }
            return viewEdit;
        }
    }
}
