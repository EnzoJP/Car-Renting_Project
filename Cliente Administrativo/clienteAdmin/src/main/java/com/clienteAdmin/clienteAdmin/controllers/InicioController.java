package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.AlquilerDTO;
import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import com.clienteAdmin.clienteAdmin.auth.AuthService;
import com.clienteAdmin.clienteAdmin.services.AlquilerService;
import com.clienteAdmin.clienteAdmin.services.EmpleadoService;
import com.clienteAdmin.clienteAdmin.services.VehiculoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class InicioController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final VehiculoService vehiculoService;

    @Autowired
    private final AlquilerService alquilerService;

    @Autowired
    private final EmpleadoService empleadoService;

    @Autowired
    public InicioController(AuthService authService, VehiculoService vehiculoService,
                            AlquilerService alquilerService, EmpleadoService empleadoService) {
        this.authService = authService;
        this.vehiculoService = vehiculoService;
        this.alquilerService = alquilerService;
        this.empleadoService = empleadoService;
    }

    @GetMapping({"/", "/login"})
    public String getLogin(Model model) {
        model.addAttribute("titulo", "Iniciar Sesión");
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            Model model) {

        boolean success = authService.login(username, password); // Mantenlo como boolean

        if (success) {
            HttpSession session = request.getSession(true);
            SecurityContext context = SecurityContextHolder.getContext();
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );
            System.out.println("Login exitoso para: " + username);
            return "redirect:/admin/dashboard";
        } else {
            System.out.println("Login fallido para: " + username);
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        authService.logout();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }

    @GetMapping({"/admin","/admin/dashboard"})
    public String home(Model model) {

        try {
            List<VehiculoDTO> vehiculos = vehiculoService.listarActivos();
            Map<String, Long> estadoFlota = vehiculos.stream()
                    .collect(Collectors.groupingBy(VehiculoDTO::getEstadoVehiculo, Collectors.counting()));

            model.addAttribute("disponibles", estadoFlota.getOrDefault("DISPONIBLE", 0L));
            model.addAttribute("alquilados", estadoFlota.getOrDefault("ALQUILADO", 0L));
            model.addAttribute("mantenimiento", estadoFlota.getOrDefault("MANTENIMIENTO", 0L));

            List<AlquilerDTO> actividadReciente = alquilerService.listarActivos().stream()
                    .sorted((a1, a2) -> a2.getFechaDesde().compareTo(a1.getFechaDesde()))
                    .limit(5)
                    .collect(Collectors.toList());

            model.addAttribute("actividadReciente", actividadReciente);

        } catch (Exception e) {
            model.addAttribute("errorDashboard", "No se pudieron cargar los datos del dashboard: " + e.getMessage());
            model.addAttribute("disponibles", 0L);
            model.addAttribute("alquilados", 0L);
            model.addAttribute("mantenimiento", 0L);
            model.addAttribute("actividadReciente", Collections.emptyList());
        }

        model.addAttribute("titulo", "Dashboard Principal");
        return "view/admin/dashboard";
    }
}