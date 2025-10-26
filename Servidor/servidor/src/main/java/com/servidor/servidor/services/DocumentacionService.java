package com.servidor.servidor.services;

import com.servidor.servidor.entities.Documentacion;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.DocumentacionRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentacionService extends BaseService<Documentacion, Long> {

    public DocumentacionService(DocumentacionRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Documentacion documentacion) throws ErrorServiceException {
        try {
            if (documentacion.getTipoDocumentacion() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de documentación");
            }
            if (documentacion.getPathArchivo() == null || documentacion.getPathArchivo().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el path del archivo");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
