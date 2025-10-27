package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService extends BaseServiceClient<VehiculoDTO, Long> {
    public VehiculoService() {
        super("http://localhost:9000/api/v1/vehiculos", VehiculoDTO.class);
    }
}
