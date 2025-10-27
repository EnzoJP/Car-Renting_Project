package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.DocumentacionDTO;
import com.clientePromo.clientePromo.services.DocumentacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDocumentacionDTOConverter implements Converter<String, DocumentacionDTO> {
    @Autowired
    private DocumentacionService documentacionService;

    @Override
    public DocumentacionDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return documentacionService.obtener(Long.parseLong(id));
    }
}
