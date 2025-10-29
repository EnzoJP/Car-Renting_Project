package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
