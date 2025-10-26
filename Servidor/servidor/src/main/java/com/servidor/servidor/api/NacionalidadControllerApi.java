package com.servidor.servidor.api;

import com.servidor.servidor.entities.Nacionalidad;
import com.servidor.servidor.services.NacionalidadService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/nacionalidades")
public class NacionalidadControllerApi extends BaseControllerApi<Nacionalidad, Long> {

    public NacionalidadControllerApi(NacionalidadService service) {
        super(service);
    }
}
