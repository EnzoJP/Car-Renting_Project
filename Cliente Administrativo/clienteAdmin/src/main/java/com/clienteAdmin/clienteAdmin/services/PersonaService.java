package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ClienteDTO;
import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import com.clienteAdmin.clienteAdmin.DTO.PersonaDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity; // Importar
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PersonaService extends BaseServiceClient<PersonaDTO, Long> {

    public PersonaService() {
        super("http://localhost:9000/api/v1/personas", PersonaDTO.class);
    }
    /**
     * Llama al endpoint del servidor /api/v1/personas/clientes-activos
     * (Incluyendo cabeceras de autenticación)
     */
    public List<ClienteDTO> listarClientesActivosOrdenados() {
        // usamos 'this.apiUrl' (heredado)
        String url = this.apiUrl + "/clientes-activos";

        try {
            HttpEntity<Void> entity = new HttpEntity<>(authService.authHeaders());

            ResponseEntity<List<ClienteDTO>> response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ClienteDTO>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al listar clientes desde el cliente: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Llama al endpoint del servidor /api/v1/personas/empleados-activos
     * (Incluyendo cabeceras de autenticación)
     */
    public List<EmpleadoDTO> listarEmpleadosActivosOrdenados() {
        // 1. Usamos 'this.apiUrl' (heredado)
        String url = this.apiUrl + "/empleados-activos";

        try {
            HttpEntity<Void> entity = new HttpEntity<>(authService.authHeaders());

            ResponseEntity<List<EmpleadoDTO>> response = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<EmpleadoDTO>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al listar empleados desde el cliente: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}