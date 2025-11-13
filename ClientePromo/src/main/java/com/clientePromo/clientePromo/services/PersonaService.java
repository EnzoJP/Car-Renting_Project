package com.clientePromo.clientePromo.services;



import com.clientePromo.clientePromo.DTO.PersonaDTO;
import org.springframework.stereotype.Service;

@Service
public class PersonaService extends BaseServiceClient<PersonaDTO, Long> {
    public PersonaService() {
        super("https://api.sprintproject.tech/api/v1/personas", PersonaDTO.class);
    }
}
