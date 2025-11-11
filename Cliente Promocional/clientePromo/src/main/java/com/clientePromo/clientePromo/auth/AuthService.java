package com.clientePromo.clientePromo.auth;

import com.clientePromo.clientePromo.DTO.ContactoDTO;
import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Getter
    private String token;
    @Getter
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

                // Convertir el Map a UsuarioDTO completo
                UsuarioDTO usuario = mapToUsuarioDTO(data);
                this.usuarioAutenticado = usuario;
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al buscar perfil: " + e.getMessage());
        }
        return false;
    }

    private UsuarioDTO mapToUsuarioDTO(Map<String, Object> data) {
        UsuarioDTO usuario = new UsuarioDTO();

        usuario.setId(data.get("id") != null ? ((Number) data.get("id")).longValue() : null);
        usuario.setNombreUsuario((String) data.get("username"));
        usuario.setNombre((String) data.get("nombre"));
        usuario.setApellido((String) data.get("apellido"));
        usuario.setRol(data.get("rol") != null ? data.get("rol").toString() : null);
        usuario.setTipoDocumento((String) data.get("tipoDocumento"));
        usuario.setNumeroDocumento((String) data.get("numeroDocumento"));

        usuario.setProvider((String) data.get("provider"));
        usuario.setProviderId((String) data.get("providerId"));
        usuario.setPictureUrl((String) data.get("pictureUrl"));
        usuario.setPerfilCompleto((Boolean) data.get("perfilCompleto"));

        // Fecha de nacimiento
        Object fechaObj = data.get("fechaNacimiento");
        if (fechaObj instanceof String fechaStr) {
            try {
                usuario.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr));
            } catch (ParseException e) {
                System.out.println("Error al parsear fecha: " + e.getMessage());
            }
        }

        // Contactos (lista)
        if (data.get("contactos") instanceof List<?> contactosList) {
            List<ContactoDTO> contactos = new ArrayList<>();
            for (Object c : contactosList) {
                if (c instanceof Map<?, ?> contactoData) {
                    ContactoDTO contacto = new ContactoDTO();
                    contacto.setId(contactoData.get("id") != null ? ((Number) contactoData.get("id")).longValue() : null);
                    contacto.setTipoContacto(contactoData.get("tipoContacto") != null ? contactoData.get("tipoContacto").toString() : null);
                    contacto.setObservacion((String) contactoData.get("observacion"));
                    contactos.add(contacto);
                }
            }
            usuario.setContactos(contactos);
        }
        //Debug
        System.out.println("=== MAPEANDO USUARIO DESDE SERVIDOR ===");
        System.out.println("Provider: " + usuario.getProvider());
        System.out.println("ProviderId: " + usuario.getProviderId());
        System.out.println("PictureUrl: " + usuario.getPictureUrl());
        System.out.println("======================================");

        return usuario;
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

    public HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    // mapear la respuesta dle JSON
    @Setter
    @Getter
    public static class TokenResponse {
        private String token;

    }
}