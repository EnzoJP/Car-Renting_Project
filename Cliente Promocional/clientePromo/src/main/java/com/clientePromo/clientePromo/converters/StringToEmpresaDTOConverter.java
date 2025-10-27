package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.EmpresaDTO;
import com.clientePromo.clientePromo.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEmpresaDTOConverter implements Converter<String, EmpresaDTO> {
    @Autowired
    private EmpresaService empresaService;

    @Override
    public EmpresaDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return empresaService.obtener(Long.parseLong(id));
    }
}
