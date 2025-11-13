package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.PaisDTO;
import com.clientePromo.clientePromo.services.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPaisDTOConverter implements Converter<String, PaisDTO> {
    @Autowired
    private PaisService paisService;

    @Override
    public PaisDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return paisService.obtener(Long.parseLong(id));
    }
}
