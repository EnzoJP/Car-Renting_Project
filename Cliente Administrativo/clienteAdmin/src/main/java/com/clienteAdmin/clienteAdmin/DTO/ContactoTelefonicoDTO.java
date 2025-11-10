package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactoTelefonicoDTO extends ContactoDTO {
    private String telefono;
    private String tipoTelefono;
}
