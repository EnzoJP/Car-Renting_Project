package com.servidor.servidor.api;

import com.servidor.servidor.entities.Alquiler;
import com.servidor.servidor.services.AlquilerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/alquileres")
public class AlquilerControllerApi extends BaseControllerApi<Alquiler, Long> {

    public AlquilerControllerApi(AlquilerService service) {
        super(service);
    }
}
