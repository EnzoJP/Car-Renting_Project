package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Promocion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocionRepository extends BaseRepository<Promocion, Long> {
    @Query("SELECT p FROM Promocion p WHERE CAST(p.fechaInicioPromocion AS java.time.LocalDate) = :fecha AND p.eliminado = false")
    List<Promocion> findPromocionesQueInicianHoy(@Param("fecha") LocalDate fecha);
}
