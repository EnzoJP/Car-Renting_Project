package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NacionalidadDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
}