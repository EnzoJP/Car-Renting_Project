package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.contactos WHERE u.id = :id")
    Optional<Usuario> findByIdWithContactos(@Param("id") Long id);
}
