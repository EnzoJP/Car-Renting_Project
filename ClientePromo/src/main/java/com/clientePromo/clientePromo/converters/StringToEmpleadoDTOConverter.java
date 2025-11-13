package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.EmpleadoDTO;
import com.clientePromo.clientePromo.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEmpleadoDTOConverter implements Converter<String, EmpleadoDTO> {
    @Autowired
    private EmpleadoService empleadoService;

    @Override
    public EmpleadoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return empleadoService.obtener(Long.parseLong(id));
    }
}
