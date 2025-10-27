package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.CostoVehiculoDTO;
import com.clientePromo.clientePromo.services.CostoVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCostoVehiculoDTOConverter implements Converter<String, CostoVehiculoDTO> {
    @Autowired
    private CostoVehiculoService costoVehiculoService;

    @Override
    public CostoVehiculoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return costoVehiculoService.obtener(Long.parseLong(id));
    }
}
