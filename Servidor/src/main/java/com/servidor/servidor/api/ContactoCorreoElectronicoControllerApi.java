package com.servidor.servidor.api;

import com.servidor.servidor.entities.ContactoCorreoElectronico;
import com.servidor.servidor.services.ContactoCorreoElectronicoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/contactocorreoelectronicos")
public class ContactoCorreoElectronicoControllerApi extends BaseControllerApi<ContactoCorreoElectronico, Long> {

    public ContactoCorreoElectronicoControllerApi(ContactoCorreoElectronicoService service) {
        super(service);
    }
}
