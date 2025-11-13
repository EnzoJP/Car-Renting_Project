package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmpresaDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private String telefono;
    private String correoElectronico;
    private DireccionDTO direccion;
    private List<ContactoDTO> contactos;
}
