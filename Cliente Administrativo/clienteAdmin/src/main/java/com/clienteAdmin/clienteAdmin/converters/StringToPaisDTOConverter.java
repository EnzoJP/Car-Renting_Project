package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.PaisDTO;
import com.clienteAdmin.clienteAdmin.services.PaisService;
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
