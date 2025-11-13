package com.servidor.servidor.entities;

import com.servidor.servidor.enums.EstadoVehiculo;
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
public class Vehiculo extends BaseEntity<Long> {
    private EstadoVehiculo estadoVehiculo;
    private String patente;

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
