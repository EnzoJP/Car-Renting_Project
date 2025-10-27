package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.ContactoDTO;
import com.clientePromo.clientePromo.services.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToContactoDTOConverter implements Converter<String, ContactoDTO> {
    @Autowired
    private ContactoService contactoService;

    @Override
    public ContactoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return contactoService.obtener(Long.parseLong(id));
    }
}
