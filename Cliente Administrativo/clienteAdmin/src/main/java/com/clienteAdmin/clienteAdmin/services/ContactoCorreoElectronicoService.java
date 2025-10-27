package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ContactoCorreoElectronicoDTO;
import org.springframework.stereotype.Service;

@Service
public class ContactoCorreoElectronicoService extends BaseServiceClient<ContactoCorreoElectronicoDTO, Long> {
    public ContactoCorreoElectronicoService() {
        super("http://localhost:9000/api/v1/contactocorreoelectronicos", ContactoCorreoElectronicoDTO.class);
    }
}
