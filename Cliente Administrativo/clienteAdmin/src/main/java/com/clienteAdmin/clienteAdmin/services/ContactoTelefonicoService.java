package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ContactoTelefonicoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoTelefonicoService extends BaseServiceClient<ContactoTelefonicoDTO, Long> {

    public ContactoTelefonicoService() {
        super("http://localhost:9000/api/v1/contactotelefonicos", ContactoTelefonicoDTO.class);
    }

    @Override
    public List<ContactoTelefonicoDTO> listarActivos() {
        // No es necesario el procesamiento adicional ahora
        return super.listarActivos();
    }

    public ContactoTelefonicoDTO altaConValidacion(ContactoTelefonicoDTO dto) throws ErrorServiceException {
        try {
            // ✅ Validaciones de número
            if (dto.getTelefono() == null || dto.getTelefono().trim().isEmpty()) {
                throw new ErrorServiceException("Debe ingresar un número de teléfono.");
            }

            if (!dto.getTelefono().matches("^[+0-9\\s-]+$")) {
                throw new ErrorServiceException("El número de teléfono solo puede contener números, espacios o '+'.");
            }

            // ✅ Validar persona asociada
            if (dto.getPersona() == null || dto.getPersona().getId() == null) {
                throw new ErrorServiceException("Debe seleccionar una persona para asociar el contacto.");
            }

            // ✅ Validar tipo de teléfono (si lo usás)
            if (dto.getTipoTelefono() == null || dto.getTipoTelefono().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el tipo de teléfono.");
            }

            // Si todo pasa, enviamos al servidor
            return super.alta(dto);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al comunicarse con el servidor.");
        }
    }
}
