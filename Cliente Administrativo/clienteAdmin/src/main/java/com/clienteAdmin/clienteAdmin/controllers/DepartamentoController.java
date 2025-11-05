package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.DepartamentoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.DepartamentoService;
import com.clienteAdmin.clienteAdmin.services.PaisService;
import com.clienteAdmin.clienteAdmin.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/departamento")
public class DepartamentoController extends BaseController<DepartamentoDTO, Long> {

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private PaisService paisService;

    public DepartamentoController(DepartamentoService service) {
        super(service);
        initController(new DepartamentoDTO(), "LIST DEPARTAMENTO", "EDIT DEPARTAMENTO");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        this.model.addAttribute("provincias", provinciaService.listarActivos());
        this.model.addAttribute("paises", paisService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        preAlta();
    }
}
