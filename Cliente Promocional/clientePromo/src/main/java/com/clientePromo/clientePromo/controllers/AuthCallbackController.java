package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthCallbackController {

    @Autowired
    private AuthService authService;

    @GetMapping("/oauth2/success")
    public String oauth2Success(@RequestParam(value = "token", required = false) String token) {

        System.out.println("=== CALLBACK OAUTH2 ===");
        System.out.println("Token recibido: " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "NULL"));

        if (token != null && !token.isEmpty()) {
            authService.setToken(token);

            // Verificar si se guardo correctamente
            System.out.println("Token guardado en AuthService: " + (authService.getToken() != null));
            System.out.println("Usuario autenticado: " + (authService.getUsuarioAutenticado() != null));

            return "redirect:/cliente/dashboard";
        }

        System.out.println("ERROR: Token es null o vacío");
        return "redirect:/login?error=no_token";
    }
}