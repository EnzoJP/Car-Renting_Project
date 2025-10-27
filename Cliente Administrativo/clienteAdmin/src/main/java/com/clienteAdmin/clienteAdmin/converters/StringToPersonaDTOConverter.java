package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.PersonaDTO;
import com.clienteAdmin.clienteAdmin.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPersonaDTOConverter implements Converter<String, PersonaDTO> {
    @Autowired
    private PersonaService personaService;

    @Override
    public PersonaDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return personaService.obtener(Long.parseLong(id));
    }
}
