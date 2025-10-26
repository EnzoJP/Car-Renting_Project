package com.servidor.servidor.services;

import com.servidor.servidor.entities.Departamento;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.DepartamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService extends BaseService<Departamento, Long> {

    public DepartamentoService(DepartamentoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Departamento departamento) throws ErrorServiceException {
        try {
            if (departamento.getNombre() == null || departamento.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del departamento");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
