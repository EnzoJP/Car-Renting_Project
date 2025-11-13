package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentacionDTO implements Identifiable<Long> {
    private Long id;
    private String tipoDocumentacion;
    private String observacion;
    private String pathArchivo;
    private String nombreArchivo;
}
