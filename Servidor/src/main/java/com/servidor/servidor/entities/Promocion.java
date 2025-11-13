package com.servidor.servidor.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Promocion extends BaseEntity<Long> {

    private double porcentajeDescuento;
    private String codigoDescuento;
    private String descripcionDescuento;
    private Date fechaInicioPromocion;
    private Date fechaFinPromocion;

    @Override
    public Long getId() { return this.id; }

    @Override
    public void setId(Long id) { this.id = id; }

    @Override
    public Boolean getEliminado() { return this.eliminado; }

    @Override
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
}