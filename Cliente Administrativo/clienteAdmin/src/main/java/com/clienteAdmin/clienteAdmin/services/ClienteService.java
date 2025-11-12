package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ClienteDTO;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends BaseServiceClient<ClienteDTO, Long> {
    public ClienteService() {
        super("https://api.sprintproject.tech/api/v1/clientes", ClienteDTO.class);
    }
}
