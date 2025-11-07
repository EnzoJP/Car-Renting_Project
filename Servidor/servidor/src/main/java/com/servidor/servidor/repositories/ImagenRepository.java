package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Imagen;
import org.springframework.data.jpa.repository.Query;

public interface ImagenRepository extends BaseRepository<Imagen, Long> {

    @Query ("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Imagen i WHERE i.nombre = :nombre")
    boolean existsByNombre(String nombre);


}
