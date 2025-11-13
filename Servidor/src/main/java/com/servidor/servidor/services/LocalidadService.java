package com.servidor.servidor.services;

import com.servidor.servidor.entities.Localidad;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.LocalidadRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalidadService extends BaseService<Localidad, Long> {

    public LocalidadService(LocalidadRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Localidad localidad) throws ErrorServiceException {
        try {
            if (localidad.getNombre() == null || localidad.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la localidad");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
