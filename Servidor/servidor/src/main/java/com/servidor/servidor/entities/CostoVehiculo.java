package com.servidor.servidor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostoVehiculo extends BaseEntity<Long> {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDesde;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaHasta;
    private double costo;

    @ManyToOne
    private CaracteristicaVehiculo caracteristicaVehiculo;

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
