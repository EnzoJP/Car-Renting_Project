package com.servidor.servidor.services;

import com.servidor.servidor.entities.Imagen;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ImagenRepository;
import org.springframework.stereotype.Service;

@Service
public class ImagenService extends BaseService<Imagen, Long> {

    public ImagenService(ImagenRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Imagen imagen) throws ErrorServiceException {
        try {
            if (imagen.getNombre() == null || imagen.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la imagen");
            }

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
