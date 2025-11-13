package com.clienteAdmin.clienteAdmin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // solo jefe admin
                        .requestMatchers(
                                "/admin/usuarios/menu",
                                "/admin/usuarios/nuevo",
                                "/admin/usuarios/editar/**",
                                "/admin/direccion/**",
                                "/generar-pdf",
                                "/generar-excel.xlsx"
                        ).hasRole("JEFE")

                        // ambos admin
                        .requestMatchers(
                                "/admin/dashboard",
                                "/admin/usuarios/clave",
                                "/admin/vehiculo/**",
                                "/admin/alquiler/**",
                                "/admin/promociones/**"
                        ).hasAnyRole("JEFE", "ADMINISTRATIVO")

                        // publico
                        .requestMatchers(
                                "/",
                                "/home",
                                "/login",
                                "/registro",
                                "/css/**",
                                "/js/**",
                                "/bootstrap/**",
                                "/assets/**",
                                "/images/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                // manejo de errores y redireecion
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/error/403");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login");
                        })
                )

                .formLogin(form -> form.disable())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // desactiva CSRF (para poder hacer POST manualmente a /login)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
