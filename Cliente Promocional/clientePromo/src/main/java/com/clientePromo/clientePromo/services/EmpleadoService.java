package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.EmpleadoDTO;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService extends BaseServiceClient<EmpleadoDTO, Long> {
    public EmpleadoService() {
        super("http://localhost:9000/api/v1/empleados", EmpleadoDTO.class);
    }
}
