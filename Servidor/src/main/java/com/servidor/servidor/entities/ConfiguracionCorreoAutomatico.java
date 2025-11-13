package com.servidor.servidor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConfiguracionCorreoAutomatico extends BaseEntity<Long> {
    private String correo;
    private String clave;
    private String puerto;
    private String smtp;
    private boolean tls;

    @OneToOne
    private Empresa empresa;

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
