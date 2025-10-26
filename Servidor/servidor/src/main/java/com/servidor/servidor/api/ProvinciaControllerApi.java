package com.servidor.servidor.api;

import com.servidor.servidor.entities.Provincia;
import com.servidor.servidor.services.ProvinciaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/provincias")
public class ProvinciaControllerApi extends BaseControllerApi<Provincia, Long> {

    public ProvinciaControllerApi(ProvinciaService service) {
        super(service);
    }
}
