package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import com.clienteAdmin.clienteAdmin.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToVehiculoDTOConverter implements Converter<String, VehiculoDTO> {
    @Autowired
    private VehiculoService vehiculoService;

    @Override
    public VehiculoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return vehiculoService.obtener(Long.parseLong(id));
    }
}
