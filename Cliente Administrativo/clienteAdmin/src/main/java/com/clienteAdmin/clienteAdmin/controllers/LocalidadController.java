package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.LocalidadDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.DepartamentoService;
import com.clienteAdmin.clienteAdmin.services.LocalidadService;
import com.clienteAdmin.clienteAdmin.services.PaisService;
import com.clienteAdmin.clienteAdmin.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/localidad")
public class LocalidadController extends BaseController<LocalidadDTO, Long> {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private PaisService paisService;

    public LocalidadController(LocalidadService service) {
        super(service);
        initController(new LocalidadDTO(), "LIST LOCALIDAD", "EDIT LOCALIDAD");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        this.model.addAttribute("departamentos", departamentoService.listarActivos());
        this.model.addAttribute("provincias", provinciaService.listarActivos());
        this.model.addAttribute("paises", paisService.listarActivos());
    }
}
