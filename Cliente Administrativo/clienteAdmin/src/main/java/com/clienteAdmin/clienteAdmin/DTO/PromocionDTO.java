package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class PromocionDTO implements Identifiable<Long> {
    private Long id;
    private double porcentajeDescuento;
    private String codigoDescuento;
    private String descripcionDescuento;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaInicioPromocion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaFinPromocion;

    @Override
    public Long getId() {
        return this.id;
    }
}