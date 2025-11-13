package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.UsuarioDTO;
import com.clienteAdmin.clienteAdmin.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUsuarioDTOConverter implements Converter<String, UsuarioDTO> {
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UsuarioDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return usuarioService.obtener(Long.parseLong(id));
    }
}
