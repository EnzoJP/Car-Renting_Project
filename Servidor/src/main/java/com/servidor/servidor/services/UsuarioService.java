package com.servidor.servidor.services;

import com.servidor.servidor.entities.Contacto;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService extends BaseService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.usuarioRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cambia la contraseña de un usuario
     *
     * @param username El username del usuario
     * @param claveActual La contraseña actual (para verificar)
     * @param claveNueva La nueva contraseña
     * @throws ErrorServiceException Si la contraseña actual no coincide o el usuario no existe
     */

    @Transactional
    public void cambiarClave(String username, String claveActual, String claveNueva)
            throws ErrorServiceException {

        try {
            System.out.println(" Procesando cambio de clave para: " + username);

            //buscar el usuario por username
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

            if (usuarioOpt.isEmpty()) {
                System.out.println(" Usuario no encontrado: " + username);
                throw new ErrorServiceException("Usuario no encontrado");
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar que la contraseña actual sea correcta
            if (!passwordEncoder.matches(claveActual, usuario.getPassword())) {
                System.out.println(" Contraseña actual incorrecta para: " + username);
                throw new ErrorServiceException("La contraseña actual es incorrecta");
            }

            // Validar que la nueva contraseña no esté vacía
            if (claveNueva == null || claveNueva.trim().isEmpty()) {
                throw new ErrorServiceException("La nueva contraseña no puede estar vacía");
            }
            if (claveNueva.length() < 3) {
                throw new ErrorServiceException("La contraseña debe tener al menos 3 caracteres");
            }

            // Encriptar y guardar la nueva contraseña
            String claveEncriptada = passwordEncoder.encode(claveNueva);
            usuario.setPassword(claveEncriptada);

            usuarioRepository.save(usuario);

            System.out.println("Contraseña actualizada exitosamente para: " + username);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error al cambiar clave: " + e.getMessage());
            e.printStackTrace();
            throw new ErrorServiceException("Error al cambiar la contraseña: " + e.getMessage());
        }
    }

    @Override
    public Optional<Usuario> modificar(Long id, Usuario usuarioNuevo) throws ErrorServiceException {
        try {
            System.out.println("=== DEBUG MODIFICAR USUARIO ===");
            System.out.println("ID: " + id);
            System.out.println("Provider recibido: " + usuarioNuevo.getProvider());
            System.out.println("ProviderId recibido: " + usuarioNuevo.getProviderId());
            System.out.println("PictureUrl recibido: " + usuarioNuevo.getPictureUrl());
            System.out.println("===============================");
            validarModificacion(usuarioNuevo);

            return repository.findById(id).map(usuarioExistente -> {
                // Actualizar campos básicos
                usuarioExistente.setNombre(usuarioNuevo.getNombre());
                usuarioExistente.setApellido(usuarioNuevo.getApellido());
                usuarioExistente.setUsername(usuarioNuevo.getUsername());

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
                // Actualizar perfilCompleto
                boolean perfilCompleto = usuarioExistente.getFechaNacimiento() != null
                        && usuarioExistente.getNumeroDocumento() != null
                        && !usuarioExistente.getNumeroDocumento().isEmpty();
                usuarioExistente.setPerfilCompleto(perfilCompleto);
                // MANEJAR CONTACTOS
                if (usuarioNuevo.getContactos() != null && !usuarioNuevo.getContactos().isEmpty()) {
                    if (usuarioExistente.getContactos() == null) {
                        usuarioExistente.setContactos(new ArrayList<>());
                    }
                    // Limpiar contactos existentes
                    usuarioExistente.getContactos().clear();
                    // Agregar nuevos contactos
                    for (Contacto contactoNuevo : usuarioNuevo.getContactos()) {
                        // Crear nueva entidad para evitar problemas de ID
                        Contacto contacto = new Contacto();
                        contacto.setTipoContacto(contactoNuevo.getTipoContacto());
                        contacto.setObservacion(contactoNuevo.getObservacion());
                        contacto.setPersona(usuarioExistente); //relación bidireccional
                        contacto.setEliminado(false);
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

    // Validación flexible para modificaciones
    private void validarModificacion(Usuario usuario) throws ErrorServiceException {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar el nombre del usuario");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar el apellido del usuario");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar el nombre de usuario");
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

    @Override
    protected void preAlta(Usuario usuario) throws ErrorServiceException {
        try {
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                String claveEncriptada = passwordEncoder.encode(usuario.getPassword());
                usuario.setPassword(claveEncriptada);
                System.out.println("Contraseña encriptada para nuevo usuario: " + usuario.getUsername());
            }
        } catch (Exception e) {
            throw new ErrorServiceException("Error al procesar la contraseña");
        }
    }
}