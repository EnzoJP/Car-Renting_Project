package com.servidor.servidor.config;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.entities.UsuarioCliente;
import com.servidor.servidor.enums.RolUsuario;
import com.servidor.servidor.jwt.JwtService;
import com.servidor.servidor.repositories.ClienteRepository;
import com.servidor.servidor.repositories.UsuarioClienteRepository;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioClienteRepository usuarioClienteRepository;

    @Value("${oauth2.success-redirect-url}")
    private String redirectBaseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // Debug
        System.out.println("=== ATRIBUTOS DE GOOGLE ===");
        oAuth2User.getAttributes().forEach((key, value) -> {
            System.out.println(key + " = " + value);
        });
        System.out.println("===========================");

        String registrationId = getRegistrationId(request);
        String email = oAuth2User.getAttribute("email");
        String providerId = getProviderId(oAuth2User, registrationId);
        String pictureUrl = getPictureUrl(oAuth2User, registrationId);
        String name = oAuth2User.getAttribute("name");
        String[] nombreCompleto = extraerNombreApellido(name);
        String nombre = nombreCompleto[0];
        String apellido = nombreCompleto[1];
        // Buscar o crear Usuario
        Usuario usuario = usuarioRepository.findByUsername(email)
                .orElseGet(() -> createNewUserFromOAuth2(
                        email, nombre, apellido, registrationId, providerId, pictureUrl
                ));
        // Actualizar imagen de perfil si cambió
        if (pictureUrl != null && !pictureUrl.equals(usuario.getPictureUrl())) {
            actualizarImagenPerfil(usuario, pictureUrl);
        }
        // Crear Cliente y vincular con Usuario
        crearYVincularCliente(usuario, nombre, apellido);
        // Generar token JWT
        String token = jwtService.getToken(usuario);
        // Redirigir con el token
        String redirectUrl = "http://localhost:8050/login-success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private void crearYVincularCliente(Usuario usuario, String nombre, String apellido) {
        // Verificar si ya existe la relación Usuario-Cliente
        Optional<UsuarioCliente> relacionExistente = usuarioClienteRepository.findByUsuarioId(usuario.getId());

        if (relacionExistente.isEmpty()) {
            // Crear nuevo Cliente
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre != null ? nombre : "Usuario");
            cliente.setApellido(apellido != null ? apellido : "");
            cliente.setDireccionEstadia("Pendiente");
            cliente.setEliminado(false);
            cliente.setFechaNacimiento(null);
            cliente.setTipoDocumento(null);
            cliente.setNumeroDocumento(null);
            cliente.setContactos(new ArrayList<>());
            cliente.setDireccion(null);
            cliente.setNacionalidad(null);
            cliente.setImagen(null);

            cliente = clienteRepository.save(cliente);
            System.out.println("Cliente creado con ID: " + cliente.getId());

            // Crear relación Usuario-Cliente
            UsuarioCliente relacion = new UsuarioCliente();
            relacion.setUsuario(usuario);
            relacion.setCliente(cliente);
            relacion.setEliminado(false);

            usuarioClienteRepository.save(relacion);
            System.out.println("Relación Usuario-Cliente creada exitosamente");
        } else {
            System.out.println("Ya existe relación para Usuario ID: " + usuario.getId());
        }
    }

    private Usuario createNewUserFromOAuth2(String email, String nombre, String apellido,
                                            String provider, String providerId, String pictureUrl) {
        // Si nombre/apellido vienen vacíos, usar parte del email
        if (nombre == null || nombre.trim().isEmpty()) {
            String[] emailParts = email.split("@");
            nombre = emailParts[0]; // Usar parte antes del @
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            apellido = ""; // Dejar vacío, se completará después
        }
        Usuario newUser = Usuario.builder()
                .username(email)
                .password("")
                .rol(RolUsuario.CLIENTE)
                .provider(provider)
                .providerId(providerId)
                .pictureUrl(pictureUrl)
                .perfilCompleto(false)
                .build();
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setImagen(null);
        newUser.setEliminado(false);
        return usuarioRepository.save(newUser);
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
