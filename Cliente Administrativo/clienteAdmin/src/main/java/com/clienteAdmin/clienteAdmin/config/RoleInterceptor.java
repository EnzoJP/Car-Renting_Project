package com.clienteAdmin.clienteAdmin.config;

import com.clienteAdmin.clienteAdmin.services.EmpleadoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
            boolean esJefe = empleadoService.esJefe();
            boolean esAdministrativo = empleadoService.esAdministrativo();

            modelAndView.addObject("esJefe", esJefe);
            modelAndView.addObject("esAdministrativo", esAdministrativo);
            modelAndView.addObject("empleadoActual", empleadoService.obtenerEmpleadoActual());
        }
    }
}