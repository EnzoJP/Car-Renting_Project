package com.servidor.servidor.services;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Empleado;
import com.servidor.servidor.entities.Persona;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService extends BaseService<Persona, Long> {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository repository) {
        super(repository);
        this.personaRepository = repository;
    }

    @Override
    protected void validar(Persona persona) throws ErrorServiceException {
        try {
            if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la persona");
            }
            if (persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido de la persona");
            }
            if (persona.getNumeroDocumento() == null || persona.getNumeroDocumento().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el número de documento de la persona");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public List<Cliente> listarClientesActivosOrdenados() throws ErrorServiceException {
        try {
            return personaRepository.findActiveClientesOrdenados();
        } catch (Exception e) {
            throw new ErrorServiceException("Error al listar clientes: " + e.getMessage());
        }
    }

    public List<Empleado> listarEmpleadosActivosOrdenados() throws ErrorServiceException {
        try {
            return personaRepository.findActiveEmpleadosOrdenados();
        } catch (Exception e) {
            throw new ErrorServiceException("Error al listar empleados: " + e.getMessage());
        }
    }
}
