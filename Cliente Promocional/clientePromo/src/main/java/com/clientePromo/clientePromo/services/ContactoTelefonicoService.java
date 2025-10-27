package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ContactoTelefonicoDTO;
import org.springframework.stereotype.Service;

@Service
public class ContactoTelefonicoService extends BaseServiceClient<ContactoTelefonicoDTO, Long> {
    public ContactoTelefonicoService() {
        super("http://localhost:9000/api/v1/contactotelefonicos", ContactoTelefonicoDTO.class);
    }
}
