package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.PaisDTO;
import com.clienteAdmin.clienteAdmin.services.PaisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pais")
public class PaisController extends BaseController<PaisDTO, Long> {

    public PaisController(PaisService service) {
        super(service);
        initController(new PaisDTO(), "LIST PAIS", "EDIT PAIS");
    }
}
