package com.servidor.servidor.api;

import com.servidor.servidor.entities.Direccion;
import com.servidor.servidor.services.DireccionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/direcciones")
public class DireccionControllerApi extends BaseControllerApi<Direccion, Long> {

    public DireccionControllerApi(DireccionService service) {
        super(service);
    }
}
