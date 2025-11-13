package com.servidor.servidor.services;

import com.servidor.servidor.entities.Pais;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.PaisRepository;
import org.springframework.stereotype.Service;

@Service
public class PaisService extends BaseService<Pais, Long> {

    public PaisService(PaisRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Pais pais) throws ErrorServiceException {
        try {
            if (pais.getNombre() == null || pais.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del país");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
