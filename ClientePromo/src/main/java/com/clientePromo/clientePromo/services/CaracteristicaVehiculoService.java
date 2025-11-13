package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.DTO.CaracteristicaVehiculoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CaracteristicaVehiculoService extends BaseServiceClient<CaracteristicaVehiculoDTO, Long> {

    public CaracteristicaVehiculoService() {
        super("https://api.sprintproject.tech/api/v1/caracteristicavehiculos", CaracteristicaVehiculoDTO.class);
    }

    /**
     * Obtiene solo los vehículos que tienen unidades disponibles
     */
    public List<CaracteristicaVehiculoDTO> obtenerVehiculosDisponibles() {
        try {
            ResponseEntity<CaracteristicaVehiculoDTO[]> response = restTemplate.exchange(
                    apiUrl + "/disponibles",
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
    private Class<CaracteristicaVehiculoDTO[]> getArrayType() {
        return (Class<CaracteristicaVehiculoDTO[]>) java.lang.reflect.Array
                .newInstance(dtoClass, 0).getClass();
    }
}