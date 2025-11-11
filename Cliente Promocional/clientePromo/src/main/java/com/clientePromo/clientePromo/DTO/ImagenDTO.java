package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
public class ImagenDTO implements Identifiable<Long> {
    private Long id;
    private String nombre;
    private String mime;
    private byte[] contenido;
    private String tipoImagen;

    //metodo para la vista
    public String getBase64() {
        if (this.contenido == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(this.contenido);
    }
}
