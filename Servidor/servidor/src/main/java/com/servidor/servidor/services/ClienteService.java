package com.servidor.servidor.services;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Contacto;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClienteService extends BaseService<Cliente, Long> {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository repository) {
        super(repository);
        this.clienteRepository = repository;
    }

    @Override
    public Optional<Cliente> modificar(Long id, Cliente clienteNuevo) throws ErrorServiceException {
        try {
            return repository.findById(id).map(clienteExistente -> {
                // Actualizar campos basicos
                clienteExistente.setNombre(clienteNuevo.getNombre());
                clienteExistente.setApellido(clienteNuevo.getApellido());
                clienteExistente.setFechaNacimiento(clienteNuevo.getFechaNacimiento());
                clienteExistente.setTipoDocumento(clienteNuevo.getTipoDocumento());
                clienteExistente.setNumeroDocumento(clienteNuevo.getNumeroDocumento());
                clienteExistente.setDireccion(clienteNuevo.getDireccion());
                clienteExistente.setImagen(clienteNuevo.getImagen());
                clienteExistente.setDireccionEstadia(clienteNuevo.getDireccionEstadia());
                clienteExistente.setNacionalidad(clienteNuevo.getNacionalidad());
                clienteExistente.setUsuarios(clienteNuevo.getUsuarios());
                // manejar contactos
                if (clienteNuevo.getContactos() != null) {
                    if (clienteExistente.getContactos() == null) {
                        clienteExistente.setContactos(new ArrayList<>());
                    }
                    clienteExistente.getContactos().clear();
                    for (Contacto contacto : clienteNuevo.getContactos()) {
                        contacto.setPersona(clienteExistente);
                        clienteExistente.getContactos().add(contacto);
                    }
                }

                return repository.save(clienteExistente);
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al modificar cliente: " + e.getMessage());
        }
    }

    @Override
    protected void validar(Cliente cliente) throws ErrorServiceException {
        try {
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del cliente");
            }
            if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido del cliente");
            }
            // Validación flexible - algunos campos pueden estar vacíos inicialmente
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

     // para vista de dashboard
    public Cliente findByUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId);
    }
}