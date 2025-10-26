package com.servidor.servidor.services;

import com.servidor.servidor.entities.FormaDePago;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.FormaDePagoRepository;
import org.springframework.stereotype.Service;

@Service
public class FormaDePagoService extends BaseService<FormaDePago, Long> {

    public FormaDePagoService(FormaDePagoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(FormaDePago formaDePago) throws ErrorServiceException {
        try {
            if (formaDePago.getTipoPago() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de pago");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
