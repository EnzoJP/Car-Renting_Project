package com.clientePromo.clientePromo.converters;

import com.clientePromo.clientePromo.DTO.ImagenDTO;
import com.clientePromo.clientePromo.services.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToImagenDTOConverter implements Converter<String, ImagenDTO> {
    @Autowired
    private ImagenService imagenService;

    @Override
    public ImagenDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return imagenService.obtener(Long.parseLong(id));
    }
}
