package com.servidor.servidor.api;

import com.servidor.servidor.entities.Empresa;
import com.servidor.servidor.services.EmpresaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/empresas")
public class EmpresaControllerApi extends BaseControllerApi<Empresa, Long> {

    public EmpresaControllerApi(EmpresaService service) {
        super(service);
    }
}
