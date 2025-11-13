package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.DTO.CostoVehiculoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CostoVehiculoService extends BaseServiceClient<CostoVehiculoDTO, Long> {

    public CostoVehiculoService() {
        super("https://api.sprintproject.tech/api/v1/costovehiculos", CostoVehiculoDTO.class);
    }

    /**
     * Lista todos los costos (sin autenticación para la página pública)
     */
    public List<CostoVehiculoDTO> listarTodosPublico() {
        try {
            ResponseEntity<CostoVehiculoDTO[]> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    getArrayType()
            );

            return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    private Class<CostoVehiculoDTO[]> getArrayType() {
        return (Class<CostoVehiculoDTO[]>) java.lang.reflect.Array
                .newInstance(dtoClass, 0).getClass();
    }

    /**
     * Obtiene el costo actual vigente para una característica de vehículo
     */
    public CostoVehiculoDTO obtenerCostoActual(Long caracteristicaId) {
        try {
            List<CostoVehiculoDTO> costos = listarTodosPublico();
            Date hoy = new Date();

            return costos.stream()
                    .filter(c -> c.getCaracteristicaVehiculo() != null
                            && c.getCaracteristicaVehiculo().getId().equals(caracteristicaId))
                    .filter(c -> !c.getFechaDesde().after(hoy)
                            && !c.getFechaHasta().before(hoy))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Formatea el costo con separador de miles
     */
    public String formatearCosto(double costo) {
        return String.format("%,.2f", costo);
    }
}