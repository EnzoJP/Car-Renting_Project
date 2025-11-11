package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class AlquilerDTO implements Identifiable<Long> {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDesde;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaHasta;
    private ClienteDTO cliente;
    private VehiculoDTO vehiculo;
    private DocumentacionDTO documentacion;
    private Double costo;
}
