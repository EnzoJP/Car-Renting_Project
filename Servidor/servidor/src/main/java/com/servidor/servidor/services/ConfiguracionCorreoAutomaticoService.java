package com.servidor.servidor.services;

import com.servidor.servidor.entities.ConfiguracionCorreoAutomatico;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ConfiguracionCorreoAutomaticoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionCorreoAutomaticoService extends BaseService<ConfiguracionCorreoAutomatico, Long> {

    public ConfiguracionCorreoAutomaticoService(ConfiguracionCorreoAutomaticoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(ConfiguracionCorreoAutomatico configuracion) throws ErrorServiceException {
        try {
            if (configuracion.getCorreo() == null || configuracion.getCorreo().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el correo");
            }
            if (configuracion.getClave() == null || configuracion.getClave().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
