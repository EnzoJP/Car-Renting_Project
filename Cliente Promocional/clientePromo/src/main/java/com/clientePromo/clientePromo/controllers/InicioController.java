package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.auth.AuthService;
import com.clientePromo.clientePromo.models.WeatherResponse;
import com.clientePromo.clientePromo.services.DolarService;
import com.clientePromo.clientePromo.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InicioController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private DolarService dolarService;

    public InicioController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("titulo", "My Car - Sprint");
        // Cargar dólar
        List<String> dolarData = dolarService.obtenerCompraVentaFecha();
        if (dolarData != null && dolarData.size() >= 2) {
            model.addAttribute("compra", dolarData.get(0));
            model.addAttribute("venta", dolarData.get(1));
            model.addAttribute("fecha", dolarData.size() > 2 ? dolarData.get(2) : "");
        }

        // Cargar clima
        WeatherResponse weather = weatherService.getWeatherByCity("Godoy Cruz").block();
        model.addAttribute("weather", weather);

        model.addAttribute("titulo", "My Car - Sprint");
        return "index";
    }

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
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }


    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("titulo", "Bienvenido al Sistema");
        return "home";
    }
}
