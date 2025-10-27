package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.ClienteDTO;
import com.clientePromo.clientePromo.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToClienteDTOConverter implements Converter<String, ClienteDTO> {
    @Autowired
    private ClienteService clienteService;

    @Override
    public ClienteDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return clienteService.obtener(Long.parseLong(id));
    }
}
