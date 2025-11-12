package com.clienteAdmin.clienteAdmin.config;

import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import com.clienteAdmin.clienteAdmin.services.EmpleadoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
// ¡IMPORTANTE! Importa estas dos clases
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Autowired
    private EmpleadoService empleadoService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {

            System.out.println("========== PRINTS (INTERCEPTOR) ==========");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean esJefe = false;
            boolean esAdministrativo = false;
            String username = null;

            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {

                // roles x spring
                esJefe = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_JEFE"));

                esAdministrativo = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATIVO"));

                // username x spring
                username = authentication.getName();
            }

            EmpleadoDTO empleado = empleadoService.obtenerEmpleadoActual();

            System.out.println("Vista: " + modelAndView.getViewName());
            System.out.println("Username (de Spring): " + username);
            System.out.println("Empleado (de API): " + (empleado != null ? empleado.getNombre() : "NULL (¡API de empleado sigue fallando!)"));
            System.out.println("esJefe (de Spring): " + esJefe);
            System.out.println("esAdministrativo (de Spring): " + esAdministrativo);
            System.out.println("========================================");

            // valores de admin
            modelAndView.addObject("esJefe", esJefe);
            modelAndView.addObject("esAdministrativo", esAdministrativo);
            modelAndView.addObject("empleadoActual", empleado);
        }
    }
}