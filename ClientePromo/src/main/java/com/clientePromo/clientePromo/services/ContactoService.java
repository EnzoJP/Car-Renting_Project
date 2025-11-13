package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ContactoDTO;
import org.springframework.stereotype.Service;

@Service
public class ContactoService extends BaseServiceClient<ContactoDTO, Long> {
    public ContactoService() {
        super("https://api.sprintproject.tech/api/v1/contactos", ContactoDTO.class);
    }
}
