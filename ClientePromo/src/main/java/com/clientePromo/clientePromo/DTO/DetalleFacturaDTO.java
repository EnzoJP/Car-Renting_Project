package com.clientePromo.clientePromo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetalleFacturaDTO implements Identifiable<Long> {
    private Long id;
    private int cantidad;
    private double subtotal;
    private List<AlquilerDTO> alquileres;
}
