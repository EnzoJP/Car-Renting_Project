package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.LocalidadDTO;
import com.clienteAdmin.clienteAdmin.services.LocalidadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/localidad")
public class LocalidadController extends BaseController<LocalidadDTO, Long> {

    public LocalidadController(LocalidadService service) {
        super(service);
        initController(new LocalidadDTO(), "LIST LOCALIDAD", "EDIT LOCALIDAD");
    }
}
