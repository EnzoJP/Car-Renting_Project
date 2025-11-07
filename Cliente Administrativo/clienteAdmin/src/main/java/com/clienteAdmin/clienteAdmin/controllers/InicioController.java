package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InicioController {

    @Autowired
    private final AuthService authService;


    public InicioController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("titulo", "My Car - Sprint");
        return "index";
    }

//    @GetMapping("/")
//    public String mostrarLogin(Model model) {
//        model.addAttribute("titulo", "Iniciar Sesión");
//        return "login";
//    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("titulo", "Iniciar Sesión");
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        boolean success = authService.login(username, password);

        if (success) {
            return "redirect:view/usuario/dashboard";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }


    // Home protegido (seria el dashboard)
    @GetMapping("/usuario/dashboard")
    public String home(Model model) {
        model.addAttribute("titulo", "Bienvenido al Sistema");
        return "view/usuario/dashboard";
    }
}
