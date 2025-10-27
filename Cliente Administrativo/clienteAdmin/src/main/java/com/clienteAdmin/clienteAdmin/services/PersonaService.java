package com.clienteAdmin.clienteAdmin.services;


import com.clienteAdmin.clienteAdmin.DTO.PersonaDTO;
import org.springframework.stereotype.Service;

@Service
public class PersonaService extends BaseServiceClient<PersonaDTO, Long> {
    public PersonaService() {
        super("http://localhost:9000/api/v1/personas", PersonaDTO.class);
    }
}
