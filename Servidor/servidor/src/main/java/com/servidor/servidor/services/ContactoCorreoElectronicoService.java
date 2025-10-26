package com.servidor.servidor.services;

import com.servidor.servidor.entities.ContactoCorreoElectronico;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ContactoCorreoElectronicoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactoCorreoElectronicoService extends BaseService<ContactoCorreoElectronico, Long> {

    public ContactoCorreoElectronicoService(ContactoCorreoElectronicoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(ContactoCorreoElectronico contacto) throws ErrorServiceException {
        try {
            if (contacto.getEmail() == null || contacto.getEmail().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el email");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
