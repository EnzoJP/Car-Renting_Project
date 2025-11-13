package com.servidor.servidor.services;

import com.servidor.servidor.entities.ContactoCorreoElectronico;
import com.servidor.servidor.entities.Persona;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ContactoCorreoElectronicoRepository;
import com.servidor.servidor.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ContactoCorreoElectronicoService extends BaseService<ContactoCorreoElectronico, Long> {

    private final ContactoCorreoElectronicoRepository correoRepository;

    @Autowired
    private PersonaRepository personaRepository; // 👈 lo inyectamos directo acá

    public ContactoCorreoElectronicoService(ContactoCorreoElectronicoRepository repository) {
        super(repository);
        this.correoRepository = repository;
    }

    @Override
    protected void validar(ContactoCorreoElectronico contacto) throws ErrorServiceException {
        try {
            if (contacto.getEmail() == null || contacto.getEmail().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el email");
            }
            // Validar formato de email con regex
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!Pattern.matches(emailRegex, contacto.getEmail())) {
                throw new ErrorServiceException("El formato del correo electrónico no es válido.");
            }

            // Evitar correos duplicados (ignorando mayúsculas)
            var existente = correoRepository.findByEmailIgnoreCase(contacto.getEmail());
            if (existente.isPresent()) {
                var c = existente.get();
                if (contacto.getId() == null || !c.getId().equals(contacto.getId())) {
                    throw new ErrorServiceException("Ya existe un contacto con ese correo electrónico.");
                }
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    @Override
    protected void preAlta(ContactoCorreoElectronico contacto) throws ErrorServiceException {
        try {
            // Caso 1: persona anidada
            if (contacto.getPersona() != null && contacto.getPersona().getId() != null) {
                Persona persona = personaRepository.findById(contacto.getPersona().getId())
                        .orElseThrow(() -> new ErrorServiceException("Persona no encontrada"));
                contacto.setPersona(persona);
                return;
            }

            // Caso 2: si el cliente mandó solo personaId (por formulario)
            // Intentamos recuperar ese id directamente mediante reflexión
            try {
                java.lang.reflect.Field personaIdField = contacto.getClass().getDeclaredField("personaId");
                personaIdField.setAccessible(true);
                Object personaIdObj = personaIdField.get(contacto);
                if (personaIdObj instanceof Number idNumber) {
                    Persona persona = personaRepository.findById(idNumber.longValue())
                            .orElseThrow(() -> new ErrorServiceException("Persona no encontrada"));
                    contacto.setPersona(persona);
                    return;
                }
            } catch (NoSuchFieldException ignored) {
                // el campo no existe en la entidad, seguimos
            }

            // Si llega acá, no se pudo asociar ninguna persona
            throw new ErrorServiceException("Debe asociar una persona al contacto.");

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al asociar la persona al contacto");
        }
    }
}
