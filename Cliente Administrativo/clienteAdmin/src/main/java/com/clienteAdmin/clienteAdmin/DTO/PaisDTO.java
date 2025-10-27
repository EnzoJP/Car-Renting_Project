package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class PaisDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
}