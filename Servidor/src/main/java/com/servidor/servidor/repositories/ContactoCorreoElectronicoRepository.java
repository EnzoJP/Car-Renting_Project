package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.ContactoCorreoElectronico;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContactoCorreoElectronicoRepository extends BaseRepository<ContactoCorreoElectronico, Long> {
    Optional<ContactoCorreoElectronico> findByEmailIgnoreCase(String email);

    @Query("SELECT c FROM ContactoCorreoElectronico c JOIN FETCH c.persona")
    List<ContactoCorreoElectronico> findAllWithPersona();
}
