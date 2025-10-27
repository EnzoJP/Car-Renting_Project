package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ProvinciaDTO;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService extends BaseServiceClient<ProvinciaDTO, Long> {
    public ProvinciaService() {
        super("http://localhost:9000/api/v1/provincias", ProvinciaDTO.class);
    }
}
