package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.ContactoTelefonico;

import java.util.Optional;

public interface ContactoTelefonicoRepository extends BaseRepository<ContactoTelefonico, Long> {
    Optional<ContactoTelefonico> findByTelefono(String telefono);
}
