package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.UsuarioDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController extends BaseController<UsuarioDTO, Long> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService service) {
        super(service);
        this.usuarioService = service;

        initController(new UsuarioDTO(),
                "LIST USUARIOS",
                "EDIT USUARIOS",
                "view/admin/Menu/usuarios/");
    }

    @GetMapping("/menu")
    public String mostrarMenuUsuarios(Model model) {
        model.addAttribute("titulo", "Gestión de Usuarios");
        return "view/admin/Menu/usuarios/detalle";
    }

    @GetMapping("/clave")
    public String cambiarClave(Model model) {
        model.addAttribute("titulo", "Cambiar Contraseña");
        return "view/admin/Menu/usuarios/cambiarClave";
    }

    @PostMapping("/clave")
    public String procesarCambiarClave(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            @RequestParam String passwordConfirm,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        String viewName = "view/admin/Menu/usuarios/cambiarClave";
        model.addAttribute("titulo", "Cambiar Contraseña");

        if (!passwordNueva.equals(passwordConfirm)) {
            model.addAttribute("error", "Las nuevas contraseñas no coinciden.");
            return viewName;
        }

        String username;
        try {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo identificar al usuario logueado.");
            return viewName;
        }

        try {
            usuarioService.cambiarClave(username, passwordActual, passwordNueva);
            redirectAttributes.addFlashAttribute("success", "Contraseña actualizada con éxito.");
            return "redirect:/admin/usuarios/clave";

        } catch (ErrorServiceException e) {
            model.addAttribute("error", e.getMessage());
            return viewName;
        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado del sistema.");
            return viewName;
        }
    }
}
