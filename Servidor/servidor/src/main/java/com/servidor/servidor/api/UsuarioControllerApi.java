package com.servidor.servidor.api;

import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioControllerApi extends BaseControllerApi<Usuario, Long> {

    private final UsuarioService usuarioService;

    public UsuarioControllerApi(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario entidad) {
        try {
            System.out.println("=== RECIBIENDO EN API ===");
            System.out.println("Provider: " + entidad.getProvider());
            System.out.println("ProviderId: " + entidad.getProviderId());
            System.out.println("PictureUrl: " + entidad.getPictureUrl());
            System.out.println("========================");
            preUpdate(entidad);
            Optional<Usuario> actualizado = service.modificar(id, entidad);

            if (actualizado.isPresent()) {
                postUpdate(actualizado.get());
                return ResponseEntity.status(HttpStatus.OK).body(actualizado.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Entidad no encontrada"));
            }
        } catch (ErrorServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al actualizar la entidad"));
        }
    }

    /**
     * Endpoint para cambiar la contraseña del usuario autenticado
     * POST /api/v1/usuarios/cambiar-clave
     */
    @PostMapping("/cambiar-clave")
    public ResponseEntity<?> cambiarClave(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        try {
            System.out.println(" Petición de cambio de clave recibida");

            // Extraer datos del body
            String username = request.get("username");
            String claveActual = request.get("claveActual");
            String claveNueva = request.get("claveNueva");

            System.out.println(" Usuario: " + username);
            System.out.println(" Autenticación: " + (authentication != null ? authentication.getName() : "null"));

            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("El username es requerido"));
            }

            if (claveActual == null || claveActual.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("La contraseña actual es requerida"));
            }

            if (claveNueva == null || claveNueva.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("La contraseña nueva es requerida"));
            }

            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("Usuario no autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Usuario no autenticado"));
            }

            // Llamar al servicio para cambiar la contraseña
            usuarioService.cambiarClave(username, claveActual, claveNueva);

            System.out.println("Contraseña cambiada exitosamente");

            return ResponseEntity.ok(new SuccessResponse("Contraseña actualizada correctamente"));

        } catch (ErrorServiceException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));

        } catch (Exception e) {
            System.out.println(" Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al cambiar la contraseña"));
        }
    }
}