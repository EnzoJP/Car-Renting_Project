package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private String mime;
    private byte[] contenido;
    private String tipoImagen;
}
