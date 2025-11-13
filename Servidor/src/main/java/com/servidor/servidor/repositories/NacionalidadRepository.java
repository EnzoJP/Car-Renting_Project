package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Nacionalidad;
import org.springframework.data.jpa.repository.Query;

public interface NacionalidadRepository extends BaseRepository<Nacionalidad, Long> {

    @Query ("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM Nacionalidad n WHERE n.nombre = :nombre")
    boolean existsByNombre(String nombre);

}
