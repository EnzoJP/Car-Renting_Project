package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.DepartamentoDTO;
import com.clienteAdmin.clienteAdmin.services.DepartamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/departamento")
public class DepartamentoController extends BaseController<DepartamentoDTO, Long> {

    public DepartamentoController(DepartamentoService service) {
        super(service);
        initController(new DepartamentoDTO(), "LIST DEPARTAMENTO", "EDIT DEPARTAMENTO");
    }
}
