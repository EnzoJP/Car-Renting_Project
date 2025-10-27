package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.CaracteristicaVehiculoDTO;
import org.springframework.stereotype.Service;

@Service
public class CaracteristicaVehiculoService extends BaseServiceClient<CaracteristicaVehiculoDTO, Long> {
    public CaracteristicaVehiculoService() {
        super("http://localhost:9000/api/v1/caracteristicavehiculos", CaracteristicaVehiculoDTO.class);
    }
}
