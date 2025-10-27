package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.NacionalidadDTO;
import org.springframework.stereotype.Service;

@Service
public class NacionalidadService extends BaseServiceClient<NacionalidadDTO, Long> {
    public NacionalidadService() {
        super("http://localhost:9000/api/v1/nacionalidades", NacionalidadDTO.class);
    }
}