package com.servidor.servidor.services;

import com.servidor.servidor.entities.ContactoTelefonico;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ContactoTelefonicoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactoTelefonicoService extends BaseService<ContactoTelefonico, Long> {

    private final ContactoTelefonicoRepository telefonoRepository;

    public ContactoTelefonicoService(ContactoTelefonicoRepository repository) {
        super(repository);
        this.telefonoRepository = repository;
    }

    @Override
    protected void validar(ContactoTelefonico contacto) throws ErrorServiceException {
        try {
            if (contacto.getTelefono() == null || contacto.getTelefono().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el teléfono");
            }
            // Validar formato numérico (+, espacios o guiones permitidos)
            if (!contacto.getTelefono().matches("^[+0-9\\s-]+$")) {
                throw new ErrorServiceException("El número de teléfono solo puede contener números, espacios o '+'.");
            }

            // Evitar duplicados
            if (telefonoRepository.findByTelefono(contacto.getTelefono())
                    .filter(t -> !t.getId().equals(contacto.getId()))
                    .isPresent()) {
                throw new ErrorServiceException("Ya existe un contacto con ese número de teléfono.");
            }

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
