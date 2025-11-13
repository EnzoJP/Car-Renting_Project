package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.DireccionDTO;
import com.clientePromo.clientePromo.services.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDireccionDTOConverter implements Converter<String, DireccionDTO> {
    @Autowired
    private DireccionService direccionService;

    @Override
    public DireccionDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return direccionService.obtener(Long.parseLong(id));
    }
}
