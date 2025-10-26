package com.servidor.servidor.services;

import com.servidor.servidor.entities.Contacto;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ContactoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactoService extends BaseService<Contacto, Long> {

    public ContactoService(ContactoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Contacto contacto) throws ErrorServiceException {
        try {
            if (contacto.getTipoContacto() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de contacto");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
