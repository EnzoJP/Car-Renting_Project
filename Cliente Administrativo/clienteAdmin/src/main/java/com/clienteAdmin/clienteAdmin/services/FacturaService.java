package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.FacturaDTO;
import org.springframework.stereotype.Service;

@Service
public class FacturaService extends BaseServiceClient<FacturaDTO, Long> {
    public FacturaService() {
        super("https://api.sprintproject.tech/api/v1/facturas", FacturaDTO.class);
    }
}


