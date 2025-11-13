package com.servidor.servidor.api;

import com.servidor.servidor.entities.ContactoTelefonico;
import com.servidor.servidor.services.ContactoTelefonicoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/contactotelefonicos")
public class ContactoTelefonicoControllerApi extends BaseControllerApi<ContactoTelefonico, Long> {

    public ContactoTelefonicoControllerApi(ContactoTelefonicoService service) {
        super(service);
    }
}
