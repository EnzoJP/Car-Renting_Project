package com.servidor.servidor.api;

import com.servidor.servidor.entities.CaracteristicaVehiculo;
import com.servidor.servidor.services.CaracteristicaVehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/caracteristicavehiculos")
public class CaracteristicaVehiculoControllerApi extends BaseControllerApi<CaracteristicaVehiculo, Long> {

    private final CaracteristicaVehiculoService caracteristicaVehiculoService;

    public CaracteristicaVehiculoControllerApi(CaracteristicaVehiculoService service) {
        super(service);
        this.caracteristicaVehiculoService = service;
    }

    @GetMapping("/disponibles")
    public ResponseEntity<?> getVehiculosDisponibles() {
        try {
            List<CaracteristicaVehiculo> todas = caracteristicaVehiculoService.listarActivos();

            List<CaracteristicaVehiculo> disponibles = todas.stream()
                    .filter(c -> (c.getCantidadTotalVehiculo() - c.getCantidadVehiculoAlquilado()) > 0)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(disponibles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener vehículos disponibles"));
        }
    }

    /**
     * Endpoint para recalcular manualmente las cantidades de vehículos
     * Útil cuando los datos se desincronizaron
     */
    @PostMapping("/recalcular-cantidades")
    public ResponseEntity<?> recalcularCantidades() {
        try {
            caracteristicaVehiculoService.recalcularTodasLasCantidades();
            return ResponseEntity.ok(new SuccessResponse("Cantidades recalculadas correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al recalcular cantidades"));
        }
    }
}