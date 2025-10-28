package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ProvinciaDTO;
import com.clienteAdmin.clienteAdmin.services.ProvinciaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/provincia")
public class ProvinciaController extends BaseController<ProvinciaDTO, Long> {

    public ProvinciaController(ProvinciaService service) {
        super(service);
        initController(new ProvinciaDTO(), "LIST PROVINCIA", "EDIT PROVINCIA");
    }
}
