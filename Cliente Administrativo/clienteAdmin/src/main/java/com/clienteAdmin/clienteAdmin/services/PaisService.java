package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.PaisDTO;
import org.springframework.stereotype.Service;

@Service
public class PaisService extends BaseServiceClient<PaisDTO, Long> {
    public PaisService() {
        super("http://localhost:9000/api/v1/paises", PaisDTO.class);
    }
}