package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.FormaDePagoDTO;
import org.springframework.stereotype.Service;

@Service
public class FormaDePagoService extends BaseServiceClient<FormaDePagoDTO, Long> {
    public FormaDePagoService() {
        super("http://localhost:9000/api/v1/formadepagos", FormaDePagoDTO.class);
    }
}
