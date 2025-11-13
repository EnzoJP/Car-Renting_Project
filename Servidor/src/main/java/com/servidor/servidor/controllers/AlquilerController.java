package com.servidor.servidor.controllers;

import com.servidor.servidor.entities.Alquiler;
import com.servidor.servidor.services.AlquilerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alquileres")
@RequiredArgsConstructor
public class AlquilerController {

    private final AlquilerService alquilerService;
    // vista dashboard
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Alquiler>> getAlquileresByCliente(@PathVariable Long idCliente) {
        List<Alquiler> alquileres = alquilerService.obtenerAlquileresPorCliente(idCliente);
        return ResponseEntity.ok(alquileres);
    }
}

