package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.LocalidadDTO;
import org.springframework.stereotype.Service;

@Service
public class LocalidadService extends BaseServiceClient<LocalidadDTO, Long> {
    public LocalidadService() {
        super("http://localhost:9000/api/v1/localidades", LocalidadDTO.class);
    }
}
