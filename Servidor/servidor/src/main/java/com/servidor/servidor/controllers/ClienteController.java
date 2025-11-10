package com.servidor.servidor.controllers;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Cliente> getClienteByUsuarioId(@PathVariable Long usuarioId) {
        Cliente cliente = clienteService.findByUsuarioId(usuarioId);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }
}