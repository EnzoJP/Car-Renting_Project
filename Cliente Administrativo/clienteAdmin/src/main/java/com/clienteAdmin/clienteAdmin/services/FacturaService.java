package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.FacturaDTO;
import org.springframework.stereotype.Service;

@Service
public class FacturaService extends BaseServiceClient<FacturaDTO, Long> {
    public FacturaService() {
        super("http://localhost:9000/api/v1/facturas", FacturaDTO.class);
    }
}


