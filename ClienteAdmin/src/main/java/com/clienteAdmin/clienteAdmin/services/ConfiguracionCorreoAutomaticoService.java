package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ConfiguracionCorreoAutomaticoDTO;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionCorreoAutomaticoService extends BaseServiceClient<ConfiguracionCorreoAutomaticoDTO, Long> {
    public ConfiguracionCorreoAutomaticoService() {
        super("https://api.sprintproject.tech/api/v1/configuracioncorreoautomaticos", ConfiguracionCorreoAutomaticoDTO.class);
    }
}


