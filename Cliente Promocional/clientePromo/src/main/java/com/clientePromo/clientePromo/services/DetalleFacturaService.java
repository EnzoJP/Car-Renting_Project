package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.DetalleFacturaDTO;
import org.springframework.stereotype.Service;

@Service
public class DetalleFacturaService extends BaseServiceClient<DetalleFacturaDTO, Long> {
    public DetalleFacturaService() {
        super("http://localhost:9000/api/v1/detallefacturas", DetalleFacturaDTO.class);
    }
}
