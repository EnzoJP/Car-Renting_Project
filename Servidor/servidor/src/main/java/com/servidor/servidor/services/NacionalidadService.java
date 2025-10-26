package com.servidor.servidor.services;

import com.servidor.servidor.entities.Nacionalidad;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.NacionalidadRepository;
import org.springframework.stereotype.Service;

@Service
public class NacionalidadService extends BaseService<Nacionalidad, Long> {

    public NacionalidadService(NacionalidadRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Nacionalidad nacionalidad) throws ErrorServiceException {
        try {
            if (nacionalidad.getNombre() == null || nacionalidad.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la nacionalidad");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
