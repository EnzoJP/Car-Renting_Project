package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ContactoCorreoElectronicoDTO;
import org.springframework.stereotype.Service;

@Service
public class ContactoCorreoElectronicoService extends BaseServiceClient<ContactoCorreoElectronicoDTO, Long> {
    public ContactoCorreoElectronicoService() {
        super("https://api.sprintproject.tech/api/v1/contactocorreoelectronicos", ContactoCorreoElectronicoDTO.class);
    }
}
