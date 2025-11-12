package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.DireccionDTO;
import org.springframework.stereotype.Service;

@Service
public class DireccionService extends BaseServiceClient<DireccionDTO, Long> {
    public DireccionService() {
        super("https://api.sprintproject.tech/api/v1/direcciones", DireccionDTO.class);
    }
}


