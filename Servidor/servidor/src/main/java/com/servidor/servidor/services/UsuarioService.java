package com.servidor.servidor.services;

import com.servidor.servidor.entities.Contacto;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService extends BaseService<Usuario, Long> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    @Override
    public Optional<Usuario> modificar(Long id, Usuario usuarioNuevo) throws ErrorServiceException {
        try {
            // Validación flexible para OAuth2
            validarModificacion(usuarioNuevo);
            return repository.findById(id).map(usuarioExistente -> {
                // Actualizar campos básicos
                usuarioExistente.setNombre(usuarioNuevo.getNombre());
                usuarioExistente.setApellido(usuarioNuevo.getApellido());
                usuarioExistente.setUsername(usuarioNuevo.getUsername());
                // Solo actualizar password si no está vacío (para OAuth2)
                if (usuarioNuevo.getPassword() != null && !usuarioNuevo.getPassword().isEmpty()) {
                    usuarioExistente.setPassword(usuarioNuevo.getPassword());
                }
                usuarioExistente.setRol(usuarioNuevo.getRol());
                usuarioExistente.setFechaNacimiento(usuarioNuevo.getFechaNacimiento());
                usuarioExistente.setTipoDocumento(usuarioNuevo.getTipoDocumento());
                usuarioExistente.setNumeroDocumento(usuarioNuevo.getNumeroDocumento());
                usuarioExistente.setDireccion(usuarioNuevo.getDireccion());
                usuarioExistente.setImagen(usuarioNuevo.getImagen());
                usuarioExistente.setProvider(usuarioNuevo.getProvider());
                usuarioExistente.setProviderId(usuarioNuevo.getProviderId());
                usuarioExistente.setPictureUrl(usuarioNuevo.getPictureUrl());
                // Actualizar perfilCompleto si tiene los datos necesarios
                boolean perfilCompleto = usuarioExistente.getFechaNacimiento() != null
                        && usuarioExistente.getNumeroDocumento() != null
                        && !usuarioExistente.getNumeroDocumento().isEmpty();
                usuarioExistente.setPerfilCompleto(perfilCompleto);

                if (usuarioNuevo.getContactos() != null) {
                    // Limpiar contactos existentes
                    if (usuarioExistente.getContactos() == null) {
                        usuarioExistente.setContactos(new ArrayList<>());
                    }
                    usuarioExistente.getContactos().clear();
                    // Agregar nuevos contactos
                    for (Contacto contacto : usuarioNuevo.getContactos()) {
                        contacto.setPersona(usuarioExistente);
                        usuarioExistente.getContactos().add(contacto);
                    }
                }

                return repository.save(usuarioExistente);
            });

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al modificar usuario: " + e.getMessage());
        }
    }

    private void validarModificacion(Usuario usuario) throws ErrorServiceException {
        try {
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del usuario");
            }
            if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido del usuario");
            }
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de usuario");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
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
