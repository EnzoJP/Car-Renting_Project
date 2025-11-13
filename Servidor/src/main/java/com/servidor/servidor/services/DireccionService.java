package com.servidor.servidor.services;

import com.servidor.servidor.entities.Direccion;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.DireccionRepository;
import org.springframework.stereotype.Service;

@Service
public class DireccionService extends BaseService<Direccion, Long> {

    public DireccionService(DireccionRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Direccion direccion) throws ErrorServiceException {
        try {
            if (direccion.getCalle() == null || direccion.getCalle().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la calle");
            }
            if (direccion.getNumeracion() == null || direccion.getNumeracion().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la numeración");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
