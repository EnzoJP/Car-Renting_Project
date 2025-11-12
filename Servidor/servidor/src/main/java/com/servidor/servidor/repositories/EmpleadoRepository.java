package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpleadoRepository extends BaseRepository<Empleado, Long> {

    @Query(value = """
        SELECT e.*, p.* FROM empleado e 
        INNER JOIN persona p ON e.id = p.id
        INNER JOIN contacto c ON c.persona_id = p.id
        INNER JOIN contacto_correo_electronico cce ON cce.id = c.id
        WHERE LOWER(cce.email) = LOWER(:email) 
        AND p.eliminado = false
        """, nativeQuery = true)
    Optional<Empleado> findByEmail(@Param("email") String email);
}
