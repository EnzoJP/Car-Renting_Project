package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class LocalidadDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private String codigoPostal;

    private DepartamentoDTO departamento;
}