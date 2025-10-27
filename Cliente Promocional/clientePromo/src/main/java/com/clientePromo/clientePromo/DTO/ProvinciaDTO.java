package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProvinciaDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private PaisDTO pais;
}