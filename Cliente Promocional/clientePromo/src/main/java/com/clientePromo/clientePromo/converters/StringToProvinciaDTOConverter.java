package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.ProvinciaDTO;
import com.clientePromo.clientePromo.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProvinciaDTOConverter implements Converter<String, ProvinciaDTO> {
    @Autowired
    private ProvinciaService provinciaService;

    @Override
    public ProvinciaDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return provinciaService.obtener(Long.parseLong(id));
    }
}
