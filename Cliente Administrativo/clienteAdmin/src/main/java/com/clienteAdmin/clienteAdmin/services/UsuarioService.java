package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.UsuarioDTO;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseServiceClient<UsuarioDTO, Long> {
    public UsuarioService() {
        super("http://localhost:9000/api/v1/usuarios", UsuarioDTO.class);
    }
}
