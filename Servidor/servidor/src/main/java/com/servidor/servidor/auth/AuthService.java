package com.servidor.servidor.auth;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.entities.UsuarioCliente;
import com.servidor.servidor.enums.RolUsuario;
import com.servidor.servidor.jwt.JwtService;
import com.servidor.servidor.repositories.ClienteRepository;
import com.servidor.servidor.repositories.UsuarioClienteRepository;
import com.servidor.servidor.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClienteRepository clienteRepository;
    private final UsuarioClienteRepository usuarioClienteRepository;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        Usuario user = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword()))
                .rol(RolUsuario.CLIENTE)
                .provider("LOCAL")
                .perfilCompleto(false)
                .build();

        userRepository.save(user);

        // Crear Cliente y vincular Usuario-Cliente (comportamiento similar a OAuth2)
        Cliente cliente = new Cliente();
        // intentar sacar un nombre básico a partir del username (parte antes del @ si existe)
        String username = request.getUsername();
        String nombre = username;
        if (username != null && username.contains("@")) {
            nombre = username.split("@")[0];
        }
        cliente.setNombre(nombre);
        cliente.setApellido("");
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

        UsuarioCliente relacion = new UsuarioCliente();
        relacion.setUsuario(user);
        relacion.setCliente(cliente);
        relacion.setEliminado(false);
        usuarioClienteRepository.save(relacion);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();

    }

}
