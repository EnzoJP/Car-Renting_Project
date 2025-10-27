package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService extends BaseServiceClient<EmpleadoDTO, Long> {
    public EmpleadoService() {
        super("http://localhost:9000/api/v1/empleados", EmpleadoDTO.class);
    }
}
