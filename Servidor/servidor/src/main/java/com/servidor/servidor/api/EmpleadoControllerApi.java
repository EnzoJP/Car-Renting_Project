package com.servidor.servidor.api;

import com.servidor.servidor.entities.Empleado;
import com.servidor.servidor.services.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/empleados")
public class EmpleadoControllerApi extends BaseControllerApi<Empleado, Long> {

    private final EmpleadoService empleadoService;

    public EmpleadoControllerApi(EmpleadoService service) {
        super(service);
        this.empleadoService = service;
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> obtenerPorUsername(@PathVariable String username) {
        try {
            Optional<Empleado> empleadoOpt = empleadoService.buscarPorUsername(username);

            if (empleadoOpt.isPresent()) {
                // Éxito: Devuelve 200 OK con el empleado
                return ResponseEntity.ok(empleadoOpt.get());
            }

            // No se encontro: Devuelve 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Empleado no encontrado con email: " + username);

        } catch (Exception e) {
            // Error real (ej. BDD caída): Devuelve 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar empleado: " + e.getMessage());
        }
    }

}