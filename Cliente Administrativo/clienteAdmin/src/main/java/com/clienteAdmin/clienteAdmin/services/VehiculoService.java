package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VehiculoService extends BaseServiceClient<VehiculoDTO, Long> {
    public VehiculoService() {
        super("http://localhost:9000/api/v1/vehiculos", VehiculoDTO.class);
    }

    public void cambiarEstado(Long vehiculoId, String nuevoEstado) throws ErrorServiceException {
        try {
            String url = apiUrl + "/" + vehiculoId + "/estado";

            Map<String, String> body = Map.of("estado", nuevoEstado);

            // prepara los headers de autenticación (usando 'authService' heredado)
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, authService.authHeaders());

            // llama a la API (usando 'restTemplate' heredado)
            restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    Void.class
            );

        } catch (Exception e) {
            throw new ErrorServiceException("Error al cambiar estado del vehículo: " + e.getMessage());
        }
    }
}
