package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class CostoVehiculoDTO implements Identifiable<Long> {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDesde;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaHasta;
    private double costo;
    private CaracteristicaVehiculoDTO caracteristicaVehiculo;
}
