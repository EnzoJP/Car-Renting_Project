package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DireccionDTO implements Identifiable<Long> {
    private  Long id;
    private String calle;
    private String numeracion;
    private String barrio;
    private String manzanaPiso;
    private String casaDepartamento;
    private String referencia;

    private LocalidadDTO localidad;
}