package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.EmpresaDTO;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService extends BaseServiceClient<EmpresaDTO, Long> {
    public EmpresaService() {
        super("https://api.sprintproject.tech/api/v1/empresas", EmpresaDTO.class);
    }
}