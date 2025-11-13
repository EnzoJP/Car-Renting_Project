package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DepartamentoDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private ProvinciaDTO provincia;
}

