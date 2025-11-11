package com.servidor.servidor.api;

import com.servidor.servidor.entities.Cliente; // Importar
import com.servidor.servidor.entities.Empleado; // Importar
import com.servidor.servidor.entities.Persona;
import com.servidor.servidor.exceptions.ErrorServiceException; // Importar
import com.servidor.servidor.services.PersonaService;
import org.springframework.http.HttpStatus; // Importar
import org.springframework.http.ResponseEntity; // Importar
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; // Importar
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List; // Importar

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/personas")
public class PersonaControllerApi extends BaseControllerApi<Persona, Long> {

    private final PersonaService personaService;

    public PersonaControllerApi(PersonaService service) {
        super(service);
        this.personaService = service;
    }

    @GetMapping("/clientes-activos")
    public ResponseEntity<?> listarClientesActivosOrdenados() {
        try {
            List<Cliente> clientes = personaService.listarClientesActivosOrdenados();
            return ResponseEntity.status(HttpStatus.OK).body(clientes);
        } catch (ErrorServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Error interno del servidor.\"}");
        }
    }

    @GetMapping("/empleados-activos")
    public ResponseEntity<?> listarEmpleadosActivosOrdenados() {
        try {
            List<Empleado> empleados = personaService.listarEmpleadosActivosOrdenados();
            return ResponseEntity.status(HttpStatus.OK).body(empleados);
        } catch (ErrorServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Error interno del servidor.\"}");
        }
    }
}