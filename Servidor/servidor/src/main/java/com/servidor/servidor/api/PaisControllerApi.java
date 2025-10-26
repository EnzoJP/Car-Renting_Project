package com.servidor.servidor.api;

import com.servidor.servidor.entities.Pais;
import com.servidor.servidor.services.PaisService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/paises")
public class PaisControllerApi extends BaseControllerApi<Pais, Long> {

    public PaisControllerApi(PaisService service) {
        super(service);
    }
}
