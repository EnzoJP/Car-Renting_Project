package com.servidor.servidor.services;

import com.servidor.servidor.entities.Contacto;
import com.servidor.servidor.entities.Persona;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ContactoRepository;
import com.servidor.servidor.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactoService extends BaseService<Contacto, Long> {

    public ContactoService(ContactoRepository repository) {
        super(repository);
    }

    @Autowired
    private PersonaRepository personaRepository;

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

    protected void preAlta(Contacto contacto) throws ErrorServiceException {
        try {
            if (contacto.getPersona() != null && contacto.getPersona().getId() != null) {
                Persona persona = personaRepository.findById(contacto.getPersona().getId())
                        .orElseThrow(() -> new ErrorServiceException("Persona no encontrada"));
                contacto.setPersona(persona);
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

}
