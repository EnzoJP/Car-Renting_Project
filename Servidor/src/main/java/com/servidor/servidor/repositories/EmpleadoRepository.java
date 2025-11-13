package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpleadoRepository extends BaseRepository<Empleado, Long> {

    //busco mail que es username
    @Query(value = """
        SELECT e.*, p.* FROM empleado e 
        INNER JOIN persona p ON e.id = p.id
        WHERE LOWER(p.username) = LOWER(:email) 
        AND p.eliminado = false
        """, nativeQuery = true)
    Optional<Empleado> findByEmail(@Param("email") String email);
}
