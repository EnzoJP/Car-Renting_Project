package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.DTO.AlquilerDTO;
import org.springframework.stereotype.Service;

@Service
public class AlquilerService extends BaseServiceClient<AlquilerDTO, Long> {
    public AlquilerService() {
        super("http://localhost:9000/api/v1/alquileres", AlquilerDTO.class);
    }
}
