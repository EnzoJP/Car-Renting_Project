package com.clientePromo.clientePromo.converters;


import com.clientePromo.clientePromo.DTO.AlquilerDTO;
import com.clientePromo.clientePromo.services.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAlquilerDTOConverter implements Converter<String, AlquilerDTO> {
    @Autowired
    private AlquilerService alquilerService;

    @Override
    public AlquilerDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return alquilerService.obtener(Long.parseLong(id));
    }
}
