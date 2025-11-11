package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.PromocionDTO;
import org.springframework.stereotype.Service;

@Service
public class PromocionService extends BaseServiceClient<PromocionDTO, Long> {
    public PromocionService() {
        super("http://localhost:9000/api/v1/promociones", PromocionDTO.class);
    }
}