package com.servidor.servidor.auth;

import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.enums.RolUsuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<Map<String, Object>> getPerfil(Authentication authentication)
    {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("id", usuario.getId());
        perfil.put("username", usuario.getUsername());
        perfil.put("rol", usuario.getRol());

        return ResponseEntity.ok(perfil);
    }

}