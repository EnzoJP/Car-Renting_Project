package com.clienteAdmin.clienteAdmin.auth;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// --- INICIO DE IMPORTACIONES NECESARIAS ---
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// --- FIN DE IMPORTACIONES ---

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private String token; // JWT almacenado tras el login

    // --- CLASES INTERNAS PARA MAPEAR LAS RESPUESTAS JSON ---

    // Para la respuesta de /auth/login
    public static class TokenResponse {
        private String token;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    // Para la respuesta de /auth/perfil
    public static class PerfilResponse {
        private String rol;
        // Agrega getters y setters
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }

    // --- FIN CLASES INTERNAS ---


    public boolean login(String username, String password) {
        String loginUrl = "https://api.sprintproject.tech/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<TokenResponse> response =
                    restTemplate.postForEntity(loginUrl, request, TokenResponse.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return false;
            }

            this.token = response.getBody().getToken();
            String perfilUrl = "https://api.sprintproject.tech/auth/perfil"; //

            HttpHeaders authHeaders = authHeaders();
            HttpEntity<Void> perfilRequest = new HttpEntity<>(authHeaders);

            ResponseEntity<PerfilResponse> perfilResponse = restTemplate.exchange(
                    perfilUrl,
                    HttpMethod.GET,
                    perfilRequest,
                    PerfilResponse.class
            );

            if (perfilResponse.getStatusCode() != HttpStatus.OK || perfilResponse.getBody() == null) {
                this.token = null;
                return false;
            }

            String rol = perfilResponse.getBody().getRol();

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;

        } catch (Exception e) {
            System.out.println("Error de login o al obtener perfil: " + e.getMessage());
            this.token = null;
        }
        return false;
    }

    public void logout() {
        this.token = null; //borra nuestro token
        SecurityContextHolder.clearContext();
    }

    public String getToken() {
        return token;
    }

    /**
     * Helper para obtener HttpHeaders con el Authorization Bearer token.
     * Útil para llamar a endpoints protegidos después de hacer login.
     */
    public HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }
        return headers;
    }
}