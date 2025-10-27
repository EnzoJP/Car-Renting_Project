package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.DireccionDTO;
import org.springframework.stereotype.Service;

@Service
public class DireccionService extends BaseServiceClient<DireccionDTO, Long> {
    public DireccionService() {
        super("http://localhost:9000/api/v1/direcciones", DireccionDTO.class);
    }
}


