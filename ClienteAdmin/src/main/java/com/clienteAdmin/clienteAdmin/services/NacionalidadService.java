package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.NacionalidadDTO;
import org.springframework.stereotype.Service;

@Service
public class NacionalidadService extends BaseServiceClient<NacionalidadDTO, Long> {
    public NacionalidadService() {
        super("https://api.sprintproject.tech/api/v1/nacionalidades", NacionalidadDTO.class);
    }
}