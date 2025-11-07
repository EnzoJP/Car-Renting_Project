package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.NacionalidadDTO;
import com.clienteAdmin.clienteAdmin.services.NacionalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nacionalidad")
public class NacionalidadController extends BaseController<NacionalidadDTO, Long> {

    @Autowired
    private NacionalidadService nacionalidadService;

    public NacionalidadController(NacionalidadService service) {
        super(service);
        initController(new NacionalidadDTO(), "LIST NACIONALIDAD", "EDIT NACIONALIDAD");
    }
}
