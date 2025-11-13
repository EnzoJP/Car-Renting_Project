package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.ConfiguracionCorreoAutomaticoDTO;
import com.clienteAdmin.clienteAdmin.services.ConfiguracionCorreoAutomaticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToConfiguracionCorreoAutomaticoDTOConverter implements Converter<String, ConfiguracionCorreoAutomaticoDTO> {
    @Autowired
    private ConfiguracionCorreoAutomaticoService configuracionCorreoAutomaticoService;

    @Override
    public ConfiguracionCorreoAutomaticoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return configuracionCorreoAutomaticoService.obtener(Long.parseLong(id));
    }
}
