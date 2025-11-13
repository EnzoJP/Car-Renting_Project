package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.NacionalidadDTO;
import com.clientePromo.clientePromo.services.NacionalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToNacionalidadDTOConverter implements Converter<String, NacionalidadDTO> {
    @Autowired
    private NacionalidadService nacionalidadService;

    @Override
    public NacionalidadDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return nacionalidadService.obtener(Long.parseLong(id));
    }
}
