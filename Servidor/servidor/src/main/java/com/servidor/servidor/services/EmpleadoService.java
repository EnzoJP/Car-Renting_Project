package com.servidor.servidor.services;

import com.servidor.servidor.entities.Empleado;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService extends BaseService<Empleado, Long> {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository repository) {
        super(repository);
        this.empleadoRepository = repository;
    }

    @Override
    protected void validar(Empleado empleado) throws ErrorServiceException {
        try {
            if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del empleado");
            }
            if (empleado.getApellido() == null || empleado.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido del empleado");
            }
            if (empleado.getNumeroDocumento() == null || empleado.getNumeroDocumento().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el número de documento del empleado");
            }
            if (empleado.getTipoEmpleado() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de empleado");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public Empleado buscarPorUsername(String username) {
        return empleadoRepository.findByEmail(username);
    }


}
