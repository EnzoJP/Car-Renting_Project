package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends BaseRepository<Cliente, Long> {

    //Cliente findByUsuariosContaining(Usuario usuario);

    //@Query("SELECT c FROM Cliente c JOIN c.usuarios u WHERE u.id = :usuarioId")
    //Optional<Cliente> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
