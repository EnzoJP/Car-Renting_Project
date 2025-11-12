package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpleadoRepository extends BaseRepository<Empleado, Long> {

    @Query(value = "SELECT e.* FROM empleado e " +
            "JOIN contacto_correo_electronico cce ON cce.id IN " +
            "(SELECT c.id FROM contacto c WHERE c.persona_id = e.id) " +
            "WHERE LOWER(cce.email) = LOWER(:email) " +
            "AND e.eliminado = false",
            nativeQuery = true)
    Empleado findByEmail(@Param("email") String email);
}
