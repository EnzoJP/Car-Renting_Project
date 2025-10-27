package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ContactoDTO;
import org.springframework.stereotype.Service;

@Service
public class ContactoService extends BaseServiceClient<ContactoDTO, Long> {
    public ContactoService() {
        super("http://localhost:9000/api/v1/contactos", ContactoDTO.class);
    }
}
