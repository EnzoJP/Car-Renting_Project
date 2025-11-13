package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseServiceClient<UsuarioDTO, Long> {
    public UsuarioService() {
        super("https://api.sprintproject.tech/api/v1/usuarios", UsuarioDTO.class);
    }
}
