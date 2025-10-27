package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.DetalleFacturaDTO;
import com.clientePromo.clientePromo.services.DetalleFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDetalleFacturaDTOConverter implements Converter<String, DetalleFacturaDTO> {
    @Autowired
    private DetalleFacturaService detalleFacturaService;

    @Override
    public DetalleFacturaDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return detalleFacturaService.obtener(Long.parseLong(id));
    }
}
