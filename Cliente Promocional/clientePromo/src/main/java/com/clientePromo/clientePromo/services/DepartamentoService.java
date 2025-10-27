package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.DepartamentoDTO;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService extends BaseServiceClient<DepartamentoDTO, Long> {
    public DepartamentoService() {
        super("http://localhost:9000/api/v1/departamentos", DepartamentoDTO.class);
    }
}
