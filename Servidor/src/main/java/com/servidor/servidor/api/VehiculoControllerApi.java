package com.servidor.servidor.api;

import com.servidor.servidor.entities.Vehiculo;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.services.VehiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/vehiculos")
public class VehiculoControllerApi extends BaseControllerApi<Vehiculo, Long> {

    private final VehiculoService vehiculoService;

    public VehiculoControllerApi(VehiculoService service) {
        super(service);
        this.vehiculoService = service;
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody EstadoRequest request) {
        try {
            System.out.println("=== Cambiando estado del vehículo ===");
            System.out.println("ID: " + id);
            System.out.println("Nuevo estado: " + request.getEstado());

            Vehiculo vehiculo = vehiculoService.cambiarEstado(id, request.getEstado());

            System.out.println("Estado cambiado exitosamente");
            return ResponseEntity.ok(vehiculo);
        } catch (ErrorServiceException e) {
            System.err.println("Error de validación: " + e.getMessage());
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado:");
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("Error al cambiar estado: " + e.getMessage()));
        }
    }

    public static class EstadoRequest {
        private String estado;
        public EstadoRequest() {}
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}