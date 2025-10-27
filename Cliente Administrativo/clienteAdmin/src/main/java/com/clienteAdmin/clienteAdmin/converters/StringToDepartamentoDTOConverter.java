package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.DepartamentoDTO;
import com.clienteAdmin.clienteAdmin.services.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDepartamentoDTOConverter implements Converter<String, DepartamentoDTO> {
    @Autowired
    private DepartamentoService departamentoService;

    @Override
    public DepartamentoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return departamentoService.obtener(Long.parseLong(id));
    }
}
