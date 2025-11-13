package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.FormaDePagoDTO;
import com.clienteAdmin.clienteAdmin.services.FormaDePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFormaDePagoDTOConverter implements Converter<String, FormaDePagoDTO> {
    @Autowired
    private FormaDePagoService formaDePagoService;

    @Override
    public FormaDePagoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return formaDePagoService.obtener(Long.parseLong(id));
    }
}
