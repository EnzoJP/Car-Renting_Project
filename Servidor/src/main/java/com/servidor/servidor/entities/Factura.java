package com.servidor.servidor.entities;

import com.servidor.servidor.enums.EstadoFactura;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factura extends BaseEntity<Long> {
    private long numeroFactura;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFactura;
    private double totalPagado;
    private EstadoFactura estado;

    @OneToMany
    private List<DetalleFactura> detalles;

    @ManyToOne
    private FormaDePago formaDePago;

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
