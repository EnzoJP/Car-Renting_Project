package com.servidor.servidor.api;

import com.servidor.servidor.entities.Departamento;
import com.servidor.servidor.services.DepartamentoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/departamentos")
public class DepartamentoControllerApi extends BaseControllerApi<Departamento, Long> {

    public DepartamentoControllerApi(DepartamentoService service) {
        super(service);
    }
}
