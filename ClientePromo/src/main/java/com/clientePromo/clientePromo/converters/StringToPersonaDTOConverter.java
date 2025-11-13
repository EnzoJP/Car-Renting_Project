package com.clientePromo.clientePromo.converters;
import com.clientePromo.clientePromo.DTO.PaisDTO;
import com.clientePromo.clientePromo.DTO.PersonaDTO;
import com.clientePromo.clientePromo.services.PersonaService;
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
