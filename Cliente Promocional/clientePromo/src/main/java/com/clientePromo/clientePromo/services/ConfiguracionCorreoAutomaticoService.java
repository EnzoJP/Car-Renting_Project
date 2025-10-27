package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ConfiguracionCorreoAutomaticoDTO;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionCorreoAutomaticoService extends BaseServiceClient<ConfiguracionCorreoAutomaticoDTO, Long> {
    public ConfiguracionCorreoAutomaticoService() {
        super("http://localhost:9000/api/v1/configuracioncorreoautomaticos", ConfiguracionCorreoAutomaticoDTO.class);
    }
}


