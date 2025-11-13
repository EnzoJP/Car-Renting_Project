package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.VehiculoDTO;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService extends BaseServiceClient<VehiculoDTO, Long> {
    public VehiculoService() {
        super("https://api.sprintproject.tech/api/v1/vehiculos", VehiculoDTO.class);
    }
}
