package com.servidor.servidor.services;

import com.servidor.servidor.entities.Empresa;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.EmpresaRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService extends BaseService<Empresa, Long> {

    public EmpresaService(EmpresaRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Empresa empresa) throws ErrorServiceException {
        try {
            if (empresa.getNombre() == null || empresa.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la empresa");
            }
            if (empresa.getTelefono() == null || empresa.getTelefono().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el teléfono de la empresa");
            }
            if (empresa.getCorreoElectronico() == null || empresa.getCorreoElectronico().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el correo electrónico de la empresa");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
