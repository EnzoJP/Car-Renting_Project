package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ContactoCorreoElectronicoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoCorreoElectronicoService extends BaseServiceClient<ContactoCorreoElectronicoDTO, Long> {

    public ContactoCorreoElectronicoService() {
        super("https://api.sprintproject.tech/api/v1/contactocorreoelectronicos", ContactoCorreoElectronicoDTO.class);
    }

    @Override
    public List<ContactoCorreoElectronicoDTO> listarActivos() {
        // Simplemente usamos el método base — ya no necesitamos reflejar nada
        return super.listarActivos();
    }

    public ContactoCorreoElectronicoDTO altaConValidacion(ContactoCorreoElectronicoDTO dto) throws ErrorServiceException {
        try {
            // ✅ Validación básica de email
            if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
                throw new ErrorServiceException("Debe ingresar una dirección de correo electrónico.");
            }

            if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new ErrorServiceException("El formato del correo electrónico no es válido.");
            }

            // ✅ Validar que haya una persona asociada
            if (dto.getPersona() == null || dto.getPersona().getId() == null) {
                throw new ErrorServiceException("Debe seleccionar una persona para asociar el contacto.");
            }

            // Si todo está bien, enviamos al servidor
            return super.alta(dto);

        } catch (ErrorServiceException e) {
            throw e; // validación esperada
        } catch (Exception e) {
            throw new ErrorServiceException("Error al comunicarse con el servidor.");
        }
    }
}
