package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UsuarioDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private String apellido;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private String tipoDocumento;
    private String numeroDocumento;
    private List<ContactoDTO> contactos;
    private DireccionDTO direccion;
    private ImagenDTO imagen;
    private String nombreUsuario;
    private String clave;
    private String rol;
    // OAuth2
    private String provider; // "GOOGLE", "FACEBOOK", "LOCAL"
    private String pictureUrl; // URL de la foto de perfil
    private Boolean perfilCompleto; // Si completó todos los datos
}
