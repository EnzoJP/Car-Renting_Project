package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactoTelefonicoDTO implements Identifiable<Long> {
    private Long id;
    private String tipoContacto;
    private String observacion;
    private String telefono;
    private String tipoTelefono;
}
