package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ProvinciaDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.PaisService;
import com.clienteAdmin.clienteAdmin.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/provincia")
public class ProvinciaController extends BaseController<ProvinciaDTO, Long> {

    @Autowired
    private PaisService paisService;

    public ProvinciaController(ProvinciaService service) {
        super(service);
        initController(new ProvinciaDTO(),
                "Listado Provincias",
                "Gestión de Provincia",
                "view/admin/Menu/direccion/");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        this.model.addAttribute("paises", paisService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        preAlta();
    }
}
