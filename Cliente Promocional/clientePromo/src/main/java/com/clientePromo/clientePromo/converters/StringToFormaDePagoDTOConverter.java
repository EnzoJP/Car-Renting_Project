package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.FormaDePagoDTO;
import com.clientePromo.clientePromo.services.FormaDePagoService;
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
