package com.servidor.servidor.entities;

import com.servidor.servidor.enums.TipoImagen;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Imagen extends BaseEntity<Long> {
    private String nombre;
    private String mime;
    @Lob
    private byte[] contenido;
    private TipoImagen tipoImagen;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Boolean getEliminado() {
        return this.eliminado;
    }

    @Override
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
