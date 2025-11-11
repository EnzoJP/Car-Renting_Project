package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.DTO.AlquilerDTO;
import com.clientePromo.clientePromo.DTO.ClienteDTO;
import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import com.clientePromo.clientePromo.auth.AuthService;
import com.clientePromo.clientePromo.models.WeatherResponse;
import com.clientePromo.clientePromo.services.AlquilerService;
import com.clientePromo.clientePromo.services.ClienteService;
import com.clientePromo.clientePromo.services.DolarService;
import com.clientePromo.clientePromo.services.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final WeatherService weatherService;
    private final DolarService dolarService;
    private final ClienteService clienteService;
    private final AlquilerService alquilerService;
    private final AuthService authService;

    @Autowired
    public ClienteController(WeatherService weatherService,
                             DolarService dolarService,
                             ClienteService clienteService,
                             AlquilerService alquilerService,
                             AuthService authService) {
        this.weatherService = weatherService;
        this.dolarService = dolarService;
        this.clienteService = clienteService;
        this.alquilerService = alquilerService;
        this.authService = authService;
    }

    @GetMapping("/dashboard")
    public String clienteDashboard(Model model) {

        try {
            //obtengo cliente autenticAado
            UsuarioDTO usuarioLogueado = authService.getUsuarioAutenticado();

            //busvo x usuario
            ClienteDTO cliente = clienteService.findByUsuarioId(usuarioLogueado.getId());
            model.addAttribute("cliente", cliente);
            model.addAttribute("usuario", usuarioLogueado);
            System.out.println(cliente.getId());
            System.out.println(usuarioLogueado.getId());
            //historial de alquileres
            List<AlquilerDTO> alquileres = alquilerService.findAlquileresPorCliente(cliente.getId());

            // calcular total solo de alquileres ADEUDADO
            double totalAdeudado = alquileres.stream()
                    .filter(a -> a.getEstadoAlquiler() == null || a.getEstadoAlquiler().name().equals("ADEUDADO") || a.getEstadoAlquiler() == com.clientePromo.clientePromo.DTO.EstadoAlquiler.ADEUDADO)
                    .mapToDouble(a -> a.getCosto() != null ? a.getCosto() : 0.0)
                    .sum();

            model.addAttribute("alquileres", alquileres);
            model.addAttribute("total", totalAdeudado);

            cargarDatosNavbar(model);

            model.addAttribute("titulo", "Mi Dashboard - Sprint");
            return "view/cliente/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo cargar tu sesión. Por favor, inicia sesión de nuevo.");
            return "redirect:/login?error=session_expired";
        }
    }

    @GetMapping("/mi-imagen")
    @ResponseBody
    public ResponseEntity<byte[]> getImagenPerfil() {

        try {
            //obtengo cliente
            UsuarioDTO usuarioLogueado = authService.getUsuarioAutenticado();
            ClienteDTO cliente = clienteService.findByUsuarioId(usuarioLogueado.getId());

            if (cliente == null ||
                    cliente.getImagen() == null ||
                    cliente.getImagen().getContenido() == null ||
                    cliente.getImagen().getMime() == null) {

                return ResponseEntity.notFound().build();
            }
            byte[] imagenBytes = cliente.getImagen().getContenido();
            String mimeType = cliente.getImagen().getMime();

            //decuelvo imagen con su tipo de contenido
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(imagenBytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void cargarDatosNavbar(Model model) {
        try {
            List<String> dolarData = dolarService.obtenerCompraVentaFecha();
            if (dolarData != null && dolarData.size() >= 2) {
                model.addAttribute("venta", dolarData.get(1));
            }
        } catch (Exception e) {
            model.addAttribute("venta", "N/A");
        }

        try {
            WeatherResponse weather = weatherService.getWeatherByCity("Godoy Cruz").block();
            model.addAttribute("weather", weather);
        } catch (Exception e) {
            model.addAttribute("weather", null);
        }
    }

}
