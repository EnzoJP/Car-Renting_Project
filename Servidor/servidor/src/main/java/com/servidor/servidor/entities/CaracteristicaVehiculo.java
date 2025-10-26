package com.servidor.servidor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaracteristicaVehiculo extends BaseEntity<Long> {
    private String marca;
    private String modelo;
    private int cantidadPuerta;
    private int cantidadAsiento;
    private long anio;
    private int cantidadTotalVehiculo;
    private int cantidadVehiculoAlquilado;

    @ManyToOne
    private Imagen imagen;

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
