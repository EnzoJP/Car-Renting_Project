package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.DTO.PromocionDTO;
import org.springframework.stereotype.Service;

@Service
public class PromocioService extends  BaseServiceClient<PromocionDTO, Long> {
    public PromocioService() {
        super("http://localhost:9000/api/v1/promociones", PromocionDTO.class);
    }
}
