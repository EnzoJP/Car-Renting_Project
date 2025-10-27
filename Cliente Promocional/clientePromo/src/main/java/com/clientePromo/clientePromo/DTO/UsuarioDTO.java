package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    private ContactoDTO contacto;
    private DireccionDTO direccion;
    private ImagenDTO imagen;
    private String nombreUsuario;
    private String clave;
    private String rol;
}
