package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.ContactoCorreoElectronicoDTO;
import com.clienteAdmin.clienteAdmin.services.ContactoCorreoElectronicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToContactoCorreoElectronicoDTOConverter implements Converter<String, ContactoCorreoElectronicoDTO> {
    @Autowired
    private ContactoCorreoElectronicoService contactoCorreoElectronicoService;

    @Override
    public ContactoCorreoElectronicoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return contactoCorreoElectronicoService.obtener(Long.parseLong(id));
    }
}
