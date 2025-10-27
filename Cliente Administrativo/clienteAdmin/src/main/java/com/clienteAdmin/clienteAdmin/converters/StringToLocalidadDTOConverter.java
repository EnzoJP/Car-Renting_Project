package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.LocalidadDTO;
import com.clienteAdmin.clienteAdmin.services.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalidadDTOConverter implements Converter<String, LocalidadDTO> {
    @Autowired
    private LocalidadService localidadService;

    @Override
    public LocalidadDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return localidadService.obtener(Long.parseLong(id));
    }
}
