package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.DTO.CaracteristicaVehiculoDTO;
import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import com.clientePromo.clientePromo.auth.AuthService;
import com.clientePromo.clientePromo.models.WeatherResponse;
import com.clientePromo.clientePromo.services.CaracteristicaVehiculoService;
import com.clientePromo.clientePromo.services.CostoVehiculoService;
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

    private final AuthService authService;
    private final WeatherService weatherService;
    private final DolarService dolarService;
    private final CaracteristicaVehiculoService caracteristicaVehiculoService;
    private final CostoVehiculoService costoVehiculoService;


    @Autowired
    public InicioController(AuthService authService,
                            WeatherService weatherService,
                            DolarService dolarService,
                            CaracteristicaVehiculoService caracteristicaVehiculoService,
                            CostoVehiculoService costoVehiculoService) {
        this.authService = authService;
        this.weatherService = weatherService;
        this.dolarService = dolarService;
        this.caracteristicaVehiculoService = caracteristicaVehiculoService;
        this.costoVehiculoService = costoVehiculoService;
    }


    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("titulo", "My Car - Sprint");

        // Cargar dolar
        List<String> dolarData = dolarService.obtenerCompraVentaFecha();
        if (dolarData != null && dolarData.size() >= 2) {
            model.addAttribute("venta", dolarData.get(1));
        }
        // Cargar clima
        WeatherResponse weather = weatherService.getWeatherByCity("Godoy Cruz").block();
        model.addAttribute("weather", weather);

        //cargar vehiculos disponibles
        List<CaracteristicaVehiculoDTO> vehiculosDisponibles =
                caracteristicaVehiculoService.obtenerVehiculosDisponibles();
        model.addAttribute("vehiculos", vehiculosDisponibles);

        //pasar el service de costos para usar en la vista
        model.addAttribute("costoService", costoVehiculoService);

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
            return "redirect:/cliente/dashboard";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    // Endpoint para recibir token de google
    @GetMapping("/login-success")
    public String loginSuccess(@RequestParam String token, Model model) {
        // Guardar el token
        authService.setToken(token);
        // Obtener usuario autenticado
        UsuarioDTO usuario = authService.getUsuarioAutenticado();
        if (usuario == null) {
            return "redirect:/login";
        }
        // Verificar si completó el perfil
        if (usuario.getFechaNacimiento() == null ||
                usuario.getNumeroDocumento() == null) {
            return "redirect:/completar-perfil";
        }
        // Perfil completo
        return "redirect:/cliente/dashboard";
    }

    @GetMapping("/register")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

}