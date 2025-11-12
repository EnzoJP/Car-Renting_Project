package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Alquiler;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AlquilerRepository extends BaseRepository<Alquiler, Long> {
   // para vista dashboard  cleinte
    List<Alquiler> findByClienteId(Long clienteId);

    @Query("SELECT a FROM Alquiler a WHERE CAST(a.fechaHasta AS java.time.LocalDate) = :fecha " +
            "AND a.eliminado = false " +
            "AND (a.estadoAlquiler IS NULL OR a.estadoAlquiler != 'PAGADO')")
    List<Alquiler> findAlquileresParaDevolverManana(@Param("fecha") LocalDate fecha);
}
