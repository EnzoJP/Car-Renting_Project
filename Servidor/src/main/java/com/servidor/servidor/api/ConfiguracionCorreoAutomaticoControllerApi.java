package com.servidor.servidor.api;

import com.servidor.servidor.entities.ConfiguracionCorreoAutomatico;
import com.servidor.servidor.services.ConfiguracionCorreoAutomaticoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/configuracioncorreoautomaticos")
public class ConfiguracionCorreoAutomaticoControllerApi extends BaseControllerApi<ConfiguracionCorreoAutomatico, Long> {

    public ConfiguracionCorreoAutomaticoControllerApi(ConfiguracionCorreoAutomaticoService service) {
        super(service);
    }
}
