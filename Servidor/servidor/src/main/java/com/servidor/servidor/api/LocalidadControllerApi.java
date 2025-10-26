package com.servidor.servidor.api;

import com.servidor.servidor.entities.Localidad;
import com.servidor.servidor.services.LocalidadService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/localidades")
public class LocalidadControllerApi extends BaseControllerApi<Localidad, Long> {

    public LocalidadControllerApi(LocalidadService service) {
        super(service);
    }
}
