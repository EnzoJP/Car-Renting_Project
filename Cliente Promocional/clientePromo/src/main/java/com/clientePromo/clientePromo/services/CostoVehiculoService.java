package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.CostoVehiculoDTO;
import org.springframework.stereotype.Service;

@Service
public class CostoVehiculoService extends BaseServiceClient<CostoVehiculoDTO, Long> {
    public CostoVehiculoService() {
        super("http://localhost:9000/api/v1/costovehiculos", CostoVehiculoDTO.class);
    }
}

