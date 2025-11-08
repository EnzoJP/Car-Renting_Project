package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
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
@RequestMapping("/admin/empleado")
public class EmpleadoController extends BaseController<EmpleadoDTO, Long> {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private DireccionService direccionService;

    public EmpleadoController(EmpleadoService service) {
        super(service);
        initController(new EmpleadoDTO(),
                "LIST EMPLEADOS",
                "EDIT EMPLEADOS",
                "view/admin/Menu/usuarios/");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }
    @Override
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("item") EmpleadoDTO entidad,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes attributes,
                             Model model) {
        try {
            this.model = model;
            preActualziacion();

            // imagen
            if (file != null && !file.isEmpty()) {
                ImagenDTO imagen = new ImagenDTO();
                imagen.setContenido(file.getBytes());
                imagen.setMime(file.getContentType());
                entidad.setImagen(imagen);
            }

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
