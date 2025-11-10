package com.servidor.servidor.config;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.enums.RolUsuario;
import com.servidor.servidor.jwt.JwtService;
import com.servidor.servidor.repositories.ClienteRepository;
import com.servidor.servidor.repositories.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Value("${oauth2.success-redirect-url}")
    private String redirectBaseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String nombre = oAuth2User.getAttribute("given_name");
        String apellido = oAuth2User.getAttribute("family_name");

        Usuario usuario = usuarioRepository.findByUsername(email)
                .orElseGet(() -> {
                    //crear uusario si no existe
                    Usuario nuevoUsuario = Usuario.builder()
                            .username(email)
                            .password("")
                            .rol(RolUsuario.CLIENTE)
                            .build();
                    nuevoUsuario.setEliminado(false);
                    return usuarioRepository.save(nuevoUsuario);
                });

        // ver si ya tiene cliente asociado la cuenta
        Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId());
        if (cliente == null) {
            // crear cliente
            cliente = new Cliente();
            cliente.setNombre(nombre != null ? nombre : "Usuario");
            cliente.setApellido(apellido != null ? apellido : "Google");
            cliente.setDireccionEstadia("Pendiente");
            cliente.setEliminado(false);

            // asociar usuario
            cliente.setUsuarios(List.of(usuario));

            clienteRepository.save(cliente);
        }

        // Generar token JWT
        String token = jwtService.getToken(usuario);

        // Redirigir con el token
        String redirectUrl = "http://localhost:8050/auth/oauth2/success?token=" + token;
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
