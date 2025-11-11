package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Empleado;
import com.servidor.servidor.entities.Persona;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonaRepository extends BaseRepository<Persona, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.eliminado = false ORDER BY c.nombre ASC, c.apellido ASC")
    List<Cliente> findActiveClientesOrdenados();

    @Query("SELECT e FROM Empleado e WHERE e.eliminado = false ORDER BY e.nombre ASC, e.apellido ASC") // <-- 'e' ES CORRECTO
    List<Empleado> findActiveEmpleadosOrdenados();
}
