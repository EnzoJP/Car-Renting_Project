package com.servidor.servidor.services;

import com.servidor.servidor.entities.ContactoTelefonico;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ContactoTelefonicoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactoTelefonicoService extends BaseService<ContactoTelefonico, Long> {

    public ContactoTelefonicoService(ContactoTelefonicoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(ContactoTelefonico contacto) throws ErrorServiceException {
        try {
            if (contacto.getTelefono() == null || contacto.getTelefono().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el teléfono");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
