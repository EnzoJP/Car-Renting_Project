package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.FacturaDTO;
import com.clientePromo.clientePromo.services.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFacturaDTOConverter implements Converter<String, FacturaDTO> {
    @Autowired
    private FacturaService facturaService;

    @Override
    public FacturaDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return facturaService.obtener(Long.parseLong(id));
    }
}
