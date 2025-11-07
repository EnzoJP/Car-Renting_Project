package com.servidor.servidor.config;

import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.enums.RolUsuario;
import com.servidor.servidor.jwt.JwtService;
import com.servidor.servidor.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // Agarra la info del usuario de Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Usuario usuario = usuarioRepository.findByUsername(email)
                .orElseGet(() -> createNewUserFromGoogle(email, name));
        // Genera token JWT para usuario iniciado con google
        String jwt = jwtService.getToken(usuario);
        // Redirige al cliente con el token en la URL
        String redirectUrl = "http://localhost:8080/login-success?token=" + jwt;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private Usuario createNewUserFromGoogle(String email, String name) {
        Usuario newUser = Usuario.builder()
                .username(email)
                .password("") //No hay clave porque viene de google
                .rol(RolUsuario.CLIENTE)
                .build();
        return usuarioRepository.save(newUser);
    }

}
