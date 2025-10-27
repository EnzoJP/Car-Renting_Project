package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.ContactoTelefonicoDTO;
import com.clientePromo.clientePromo.services.ContactoTelefonicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToContactoTelefonicoDTOConverter implements Converter<String, ContactoTelefonicoDTO> {
    @Autowired
    private ContactoTelefonicoService contactoTelefonicoService;

    @Override
    public ContactoTelefonicoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return contactoTelefonicoService.obtener(Long.parseLong(id));
    }
}
