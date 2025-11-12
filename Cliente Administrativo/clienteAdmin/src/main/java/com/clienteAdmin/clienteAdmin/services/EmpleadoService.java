package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService extends BaseServiceClient<EmpleadoDTO, Long> {

    public EmpleadoService() {
        super("http://localhost:9000/api/v1/empleados", EmpleadoDTO.class);
    }

    public EmpleadoDTO obtenerEmpleadoActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String username = auth.getName();
            return obtenerPorUsername(username);
        }
        return null;
    }

    public EmpleadoDTO obtenerPorUsername(String username) {
        try {
            HttpEntity<Void> entity = new HttpEntity<>(authService.authHeaders());

            ResponseEntity<EmpleadoDTO> response = restTemplate.exchange(
                    apiUrl + "/username/" + username,
                    HttpMethod.GET,
                    entity,
                    EmpleadoDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al obtener empleado por username: " + e.getMessage());
            return null;
        }
    }
    public boolean esJefe() {
        EmpleadoDTO empleado = obtenerEmpleadoActual();
        return empleado != null && "JEFE".equals(empleado.getTipoEmpleado());
    }

    public boolean esAdministrativo() {
        EmpleadoDTO empleado = obtenerEmpleadoActual();
        return empleado != null && "ADMINISTRATIVO".equals(empleado.getTipoEmpleado());
    }

    public String getTipoEmpleadoActual() {
        EmpleadoDTO empleado = obtenerEmpleadoActual();
        return empleado != null ? empleado.getTipoEmpleado() : null;
    }
}