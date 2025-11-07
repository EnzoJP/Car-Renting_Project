package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import com.clienteAdmin.clienteAdmin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empleado")
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
        initController(new EmpleadoDTO(), "LIST EMPLEADOS", "EDIT EMPLEADOS");
    }

    @Override
    protected void preAlta() {
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }

    @Override
    protected void preModificacion() {
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }
}
