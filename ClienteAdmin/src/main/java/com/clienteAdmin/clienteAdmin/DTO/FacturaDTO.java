package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FacturaDTO implements Identifiable<Long> {
    private Long id;
    private long numeroFactura;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFactura;
    private double totalPagado;
    private String estado;
    private List<DetalleFacturaDTO> detalles;
    private FormaDePagoDTO formaDePago;
}
