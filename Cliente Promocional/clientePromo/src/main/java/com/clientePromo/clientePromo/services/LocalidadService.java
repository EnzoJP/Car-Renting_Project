package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.LocalidadDTO;
import org.springframework.stereotype.Service;

@Service
public class LocalidadService extends BaseServiceClient<LocalidadDTO, Long> {
    public LocalidadService() {
        super("http://localhost:9000/api/v1/localidades", LocalidadDTO.class);
    }
}
