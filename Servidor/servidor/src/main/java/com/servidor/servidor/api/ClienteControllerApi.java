package com.servidor.servidor.api;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.UsuarioCliente;
import com.servidor.servidor.repositories.UsuarioClienteRepository;
import com.servidor.servidor.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/clientes")
public class ClienteControllerApi extends BaseControllerApi<Cliente, Long> {


    private final ClienteService clienteService;
    private final UsuarioClienteRepository usuarioClienteRepository;

    public ClienteControllerApi(ClienteService service, UsuarioClienteRepository usuarioClienteRepository) {
        super(service);
        this.clienteService = service;
        this.usuarioClienteRepository = usuarioClienteRepository;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getClienteByUsuarioId(@PathVariable Long usuarioId) {
        try {
            Optional<UsuarioCliente> relacion = usuarioClienteRepository.findByUsuarioId(usuarioId);

            if (relacion.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Cliente cliente = relacion.get().getCliente();
            return ResponseEntity.ok(cliente);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
