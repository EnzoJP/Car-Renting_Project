package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ProvinciaDTO;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService extends BaseServiceClient<ProvinciaDTO, Long> {
    public ProvinciaService() {
        super("https://api.sprintproject.tech/api/v1/provincias", ProvinciaDTO.class);
    }
}
