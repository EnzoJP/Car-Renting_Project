package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonaDTO implements Identifiable<Long> {
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
}
