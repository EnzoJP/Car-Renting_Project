package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.CostoVehiculoDTO;
import org.springframework.stereotype.Service;

@Service
public class CostoVehiculoService extends BaseServiceClient<CostoVehiculoDTO, Long> {
    public CostoVehiculoService() {
        super("https://api.sprintproject.tech/api/v1/costovehiculos", CostoVehiculoDTO.class);
    }
}

