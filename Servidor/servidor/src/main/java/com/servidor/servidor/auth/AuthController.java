package com.servidor.servidor.auth;

import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.enums.RolUsuario;
import com.servidor.servidor.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {

        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping(value = "/perfil")
    @Transactional
    public ResponseEntity<Map<String, Object>> getPerfil(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Usuario usuarioCompleto = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("id", usuario.getId());
        perfil.put("username", usuario.getUsername());
        perfil.put("nombre", usuario.getNombre());
        perfil.put("apellido", usuario.getApellido());
        perfil.put("rol", usuario.getRol());
        perfil.put("provider", usuario.getProvider());
        perfil.put("pictureUrl", usuario.getPictureUrl());
        perfil.put("perfilCompleto", usuario.getPerfilCompleto());

        // Datos de Persona (pueden ser null)
        perfil.put("fechaNacimiento", usuario.getFechaNacimiento());
        perfil.put("tipoDocumento", usuario.getTipoDocumento());
        perfil.put("numeroDocumento", usuario.getNumeroDocumento());

        // Contacto (si existe)
        if (usuarioCompleto.getContactos() != null && !usuarioCompleto.getContactos().isEmpty()) {
            var contactosList = usuarioCompleto.getContactos().stream().map(c -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", c.getId());
                m.put("tipoContacto", c.getTipoContacto());
                m.put("observacion", c.getObservacion());
                return m;
            }).collect(Collectors.toList());
            perfil.put("contactos", contactosList);
        } else {
            perfil.put("contactos", List.of());
        }

        return ResponseEntity.ok(perfil);
    }

}