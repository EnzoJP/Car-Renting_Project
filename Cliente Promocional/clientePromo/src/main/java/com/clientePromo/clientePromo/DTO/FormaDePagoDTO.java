package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaDePagoDTO implements Identifiable<Long> {
    private Long id;
    private String tipoPago;
    private String observacion;
}
