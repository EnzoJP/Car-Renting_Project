package com.servidor.servidor.services;

import com.servidor.servidor.entities.Provincia;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ProvinciaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService extends BaseService<Provincia, Long> {

    public ProvinciaService(ProvinciaRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Provincia provincia) throws ErrorServiceException {
        try {
            if (provincia.getNombre() == null || provincia.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la provincia");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
