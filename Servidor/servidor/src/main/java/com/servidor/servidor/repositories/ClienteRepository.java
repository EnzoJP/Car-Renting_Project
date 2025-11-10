package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends BaseRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c JOIN c.usuarios u WHERE u.id = :usuarioId")
    Cliente findByUsuarioId(@Param("usuarioId") Long usuarioId);

    Cliente findByUsuariosContaining(Usuario usuario);
}
