package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.PromocionDTO;
import org.springframework.stereotype.Service;

@Service
public class PromocionService extends BaseServiceClient<PromocionDTO, Long> {
    public PromocionService() {
        super("https://api.sprintproject.tech/api/v1/promociones", PromocionDTO.class);
    }
}