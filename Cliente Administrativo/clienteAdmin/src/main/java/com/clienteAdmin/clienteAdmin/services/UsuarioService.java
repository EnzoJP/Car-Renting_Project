package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.UsuarioDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsuarioService extends BaseServiceClient<UsuarioDTO, Long> {
    public UsuarioService() {
        super("https://api.sprintproject.tech/api/v1/usuarios", UsuarioDTO.class);
    }

    public void cambiarClave(String username, String claveActual, String claveNueva) throws ErrorServiceException {
        try {
            // 'apiUrl' (del padre)
            String url = apiUrl + "/cambiar-clave";

            Map<String, String> body = Map.of(
                    "username", username,
                    "claveActual", claveActual,
                    "claveNueva", claveNueva
            );

            // usa 'authService' (del padre) para enviar el Token JWT
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, authService.authHeaders());

            // 4. USA 'restTemplate' (del padre) para hacer la llamada
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

        } catch (Exception e) {
            throw new ErrorServiceException("Error al cambiar la clave: " + e.getMessage());
        }
    }
}
