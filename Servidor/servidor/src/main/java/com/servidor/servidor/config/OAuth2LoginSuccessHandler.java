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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Value("${oauth2.success-redirect-url}")
    private String redirectBaseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // Obtener información del usuario desde OAuth2User
        String registrationId = getRegistrationId(request); // obtener proveedor
        String email = oAuth2User.getAttribute("email");
        String providerId = getProviderId(oAuth2User, registrationId);
        String pictureUrl = getPictureUrl(oAuth2User, registrationId);
        String name = oAuth2User.getAttribute("name");
        String[] nombreCompleto = extraerNombreApellido(name);
        String nombre = nombreCompleto[0];
        String apellido = nombreCompleto[1];

        Usuario usuario = usuarioRepository.findByUsername(email) //Busca o crea usuario
                .orElseGet(() -> createNewUserFromOAuth2(
                        email,
                        nombre,
                        apellido,
                        registrationId,
                        providerId,
                        pictureUrl
                ));

        if (pictureUrl != null && !pictureUrl.equals(usuario.getPictureUrl())) { // Actualizar la imagen de perfil si ha cambiado
            actualizarImagenPerfil(usuario, pictureUrl);
        }

        // ver si ya tiene cliente asociado la cuenta y crearlo si no existe
        crearClienteSiNoExiste(usuario, nombre, apellido);

        // Generar token JWT
        String token = jwtService.getToken(usuario);

        // Redirigir con el token
        String redirectUrl = "http://localhost:8050/login-success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private Usuario createNewUserFromOAuth2(String email, String nombre, String apellido, String provider, String providerId, String pictureUrl) {
        Usuario newUser = Usuario.builder()
                .username(email)
                .password("") // Sin password para google o facebook
                .rol(RolUsuario.CLIENTE)
                .provider(provider)
                .providerId(providerId)
                .pictureUrl(pictureUrl) // Solo la URL
                .perfilCompleto(false)
                .build();

        newUser.setNombre(nombre != null ? nombre : "Usuario");
        newUser.setApellido(apellido != null ? apellido : "");
        newUser.setImagen(null); // No descargar imagen inicialmente
        newUser.setEliminado(false);

        return usuarioRepository.save(newUser);
    }

    private void crearClienteSiNoExiste(Usuario usuario, String nombre, String apellido) {
        // verifica si ya tiene Cliente asociado
        Cliente cliente = clienteRepository.findByUsuariosContaining(usuario);
        if (cliente == null) {
            // Crear nuevo Cliente
            cliente = new Cliente();
            cliente.setNombre(nombre != null ? nombre : "Usuario");
            cliente.setApellido(apellido != null ? apellido : "");
            cliente.setDireccionEstadia("Pendiente");
            cliente.setEliminado(false);
            // Datos heredados de Persona que quedan null hasta completar perfil
            cliente.setFechaNacimiento(null);
            cliente.setTipoDocumento(null);
            cliente.setNumeroDocumento(null);
            cliente.setContactos(new ArrayList<>());
            cliente.setDireccion(null);
            cliente.setNacionalidad(null);
            cliente.setImagen(null);

            // Asociar Usuario al Cliente
            List<Usuario> usuarios = new ArrayList<>();
            usuarios.add(usuario);
            cliente.setUsuarios(usuarios);

            clienteRepository.save(cliente);
        }
    }

    private void actualizarImagenPerfil(Usuario usuario, String pictureUrl) {
        // Solo actualizar la URL, no descargar
        usuario.setPictureUrl(pictureUrl);
        usuarioRepository.save(usuario);
    }

    private String getRegistrationId(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("google")) {
            return "GOOGLE";
        } else if (uri.contains("facebook")) {
            return "FACEBOOK";
        }
        return "UNKNOWN";
    }

    private String getProviderId(OAuth2User oAuth2User, String registrationId) {
        if ("GOOGLE".equals(registrationId)) {
            return oAuth2User.getAttribute("sub");
        } else if ("FACEBOOK".equals(registrationId)) {
            return oAuth2User.getAttribute("id");
        }
        return null;
    }

    private String getPictureUrl(OAuth2User oAuth2User, String registrationId) {
        if ("GOOGLE".equals(registrationId)) {
            return oAuth2User.getAttribute("picture");
        } else if ("FACEBOOK".equals(registrationId)) {
            Map<String, Object> picture = oAuth2User.getAttribute("picture");
            if (picture != null) {
                Map<String, Object> data = (Map<String, Object>) picture.get("data");
                if (data != null) {
                    return (String) data.get("url");
                }
            }
        }
        return null;
    }

    private String[] extraerNombreApellido(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return new String[]{"", ""};
        }

        String[] partes = nombreCompleto.trim().split("\\s+", 2);
        String nombre = partes[0];
        String apellido = partes.length > 1 ? partes[1] : "";

        return new String[]{nombre, apellido};
    }




}
