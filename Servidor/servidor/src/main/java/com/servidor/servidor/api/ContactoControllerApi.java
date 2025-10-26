package com.servidor.servidor.api;

import com.servidor.servidor.entities.Contacto;
import com.servidor.servidor.services.ContactoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/contactos")
public class ContactoControllerApi extends BaseControllerApi<Contacto, Long> {

    public ContactoControllerApi(ContactoService service) {
        super(service);
    }
}
