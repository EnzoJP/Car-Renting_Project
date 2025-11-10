package com.clientePromo.clientePromo.auth;

import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private String token;
    private UsuarioDTO usuarioAutenticado;

    public boolean login(String username, String password) {
        String url = "http://localhost:9000/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<TokenResponse> response =
                    restTemplate.postForEntity(url, request, TokenResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                this.token = response.getBody().getToken();
                System.out.println(" Token guardado: " + this.token);
                // con token busco perfil
                return fetchUsuarioAutenticado();
            }
        } catch (Exception e) {
            System.out.println("Error de login: " + e.getMessage());
        }
        return false;
    }

        //buscamos perfil
        private boolean fetchUsuarioAutenticado() {
            String url = "http://localhost:9000/auth/perfil";

            try {
                HttpEntity<Void> entity = new HttpEntity<>(authHeaders());


                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    Map<String, Object> data = response.getBody();

                    // Convertir el Map a UsuarioDTO
                    UsuarioDTO usuario = new UsuarioDTO();
                    usuario.setId(((Number) data.get("id")).longValue());
                    usuario.setNombreUsuario((String) data.get("username"));
                    usuario.setRol(data.get("rol").toString());

                    this.usuarioAutenticado = usuario;
                    return true;
                }

            } catch (Exception e) {
                System.out.println("Error al buscar perfil: " + e.getMessage());
            }
            return false;
        }

    public String getToken() {
        return token;
    }

    // Modificamos setToken para que también busque el perfil (para el login de Google)
    public void setToken(String token) {
        this.token = token;
        if (token != null) {
            fetchUsuarioAutenticado();
        } else {
            this.usuarioAutenticado = null;
        }
    }

    public UsuarioDTO getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    // mapear la respuesta dle JSON
    public static class TokenResponse {
        private String token;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}