package com.clientePromo.clientePromo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/inicio", "/login", "/login-success", "/auth/oauth2/success","/css/**", "/js/**", "/bootstrap/**", "/images/**").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable());
        return http.build();
    }
}

