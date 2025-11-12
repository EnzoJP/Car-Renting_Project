package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VehiculoService extends BaseServiceClient<VehiculoDTO, Long> {
    public VehiculoService() {
        super("https://api.sprintproject.tech/api/v1/vehiculos", VehiculoDTO.class);
    }

    public void cambiarEstado(Long vehiculoId, String nuevoEstado) {
        try {
            String url = apiUrl + "/" + vehiculoId + "/estado";

            HttpHeaders headers = authService.authHeaders();

            Map<String, String> body = new HashMap<>();
            body.put("estado", nuevoEstado);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(url, HttpMethod.PUT, entity, VehiculoDTO.class);

            System.out.println("Estado del vehículo " + vehiculoId + " cambiado a: " + nuevoEstado);

        } catch (Exception e) {
            System.err.println("Error al cambiar estado del vehículo: " + e.getMessage());
            throw new RuntimeException("Error al cambiar estado del vehículo: " + e.getMessage());
        }
    }
}
