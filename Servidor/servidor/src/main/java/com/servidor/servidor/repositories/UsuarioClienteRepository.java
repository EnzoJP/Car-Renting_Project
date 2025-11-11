package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.UsuarioCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioClienteRepository extends JpaRepository<UsuarioCliente, Long> {

    @Query("SELECT uc FROM UsuarioCliente uc WHERE uc.usuario.id = :usuarioId")
    Optional<UsuarioCliente> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT uc FROM UsuarioCliente uc WHERE uc.cliente.id = :clienteId")
    Optional<UsuarioCliente> findByClienteId(@Param("clienteId") Long clienteId);
}
