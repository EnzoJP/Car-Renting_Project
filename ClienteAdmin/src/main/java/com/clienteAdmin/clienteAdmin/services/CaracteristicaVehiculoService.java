package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.CaracteristicaVehiculoDTO;
import org.springframework.stereotype.Service;

@Service
public class CaracteristicaVehiculoService extends BaseServiceClient<CaracteristicaVehiculoDTO, Long> {
    public CaracteristicaVehiculoService() {
        super("https://api.sprintproject.tech/api/v1/caracteristicavehiculos", CaracteristicaVehiculoDTO.class);
    }
}
