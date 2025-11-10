package com.servidor.servidor.services;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends BaseService<Cliente, Long> {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository repository) {
        super(repository);
        this.clienteRepository = repository;
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
            if (cliente.getNumeroDocumento() == null || cliente.getNumeroDocumento().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el número de documento del cliente");
            }
            if (cliente.getDireccionEstadia() == null || cliente.getDireccionEstadia().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la dirección de estadía del cliente");
            }
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