package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.ClienteDTO;
import com.clienteAdmin.clienteAdmin.services.ClienteService;
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
