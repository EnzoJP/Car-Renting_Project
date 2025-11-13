package com.servidor.servidor.services;

import com.servidor.servidor.entities.Imagen;
import com.servidor.servidor.enums.TipoImagen;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.ImagenRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

            // duplicados chequeo

            if (imagen.getNombre() != null && !imagen.getNombre().isEmpty()) {
                boolean exists = ((ImagenRepository) repository).existsByNombre(imagen.getNombre());
                if (exists) {
                    throw new ErrorServiceException("Ya existe una imagen con ese nombre");
                }
            }

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    // ---- Imagenes de perfiles de google o facebook ----

    public Imagen descargarYGuardarImagenPerfil(String imageUrl, String nombreUsuario) {
        try {
            // Descargar imagen desde URL
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();
            // crear imagen a partir de la descargada
            Imagen imagen = new Imagen();
            imagen.setNombre("perfil_" + nombreUsuario);
            imagen.setMime("image/jpeg");
            imagen.setContenido(imageBytes);
            imagen.setTipoImagen(TipoImagen.PERSONA);
            imagen.setEliminado(false);
            return repository.save(imagen);

        } catch (IOException e) {
            System.err.println("Error al descargar imagen: " + e.getMessage());
            return null;
        }
    }

    public Imagen actualizarImagenPerfil(Long imagenId, String imageUrl, String nombreUsuario) {
        try {
            Imagen imagenExistente = repository.findById(imagenId).orElse(null);
            if (imagenExistente == null) {
                return descargarYGuardarImagenPerfil(imageUrl, nombreUsuario);
            }
            // Descargar nueva imagen
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();

            imagenExistente.setContenido(imageBytes);
            return repository.save(imagenExistente);
        } catch (IOException e) {
            System.err.println("Error al actualizar imagen: " + e.getMessage());
            return null;
        }
    }
}
