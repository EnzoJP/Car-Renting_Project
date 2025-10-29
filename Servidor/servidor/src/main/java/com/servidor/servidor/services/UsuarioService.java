package com.servidor.servidor.services;

import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<Usuario, Long> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Usuario usuario) throws ErrorServiceException {
        try {
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del usuario");
            }
            if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido del usuario");
            }
            if (usuario.getNumeroDocumento() == null || usuario.getNumeroDocumento().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el número de documento del usuario");
            }
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de usuario");
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la clave del usuario");
            }
            if (usuario.getRol() == null) {
                throw new ErrorServiceException("Debe indicar el rol del usuario");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
