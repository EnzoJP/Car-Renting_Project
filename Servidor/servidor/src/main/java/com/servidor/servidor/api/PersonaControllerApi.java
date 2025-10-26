package com.servidor.servidor.api;

import com.servidor.servidor.entities.Persona;
import com.servidor.servidor.services.PersonaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/personas")
public class PersonaControllerApi extends BaseControllerApi<Persona, Long> {

    public PersonaControllerApi(PersonaService service) {
        super(service);
    }
}
