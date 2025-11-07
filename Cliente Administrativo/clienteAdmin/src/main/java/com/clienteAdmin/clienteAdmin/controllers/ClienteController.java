package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ClienteDTO;
import com.clienteAdmin.clienteAdmin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
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
        initController(new ClienteDTO(), "LIST CLIENTES", "EDIT CLIENTES");
    }

    @Override
    protected void preAlta() {
        model.addAttribute("nacionalidades", nacionalidadService.listarActivos());
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }

    @Override
    protected void preModificacion() {
        model.addAttribute("nacionalidades", nacionalidadService.listarActivos());
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());
    }
}
