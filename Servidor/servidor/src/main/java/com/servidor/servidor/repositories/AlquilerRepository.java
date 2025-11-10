package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Alquiler;

import java.util.List;

public interface AlquilerRepository extends BaseRepository<Alquiler, Long> {
   // para vista dashboard  cleinte
    List<Alquiler> findByClienteId(Long clienteId);
}
