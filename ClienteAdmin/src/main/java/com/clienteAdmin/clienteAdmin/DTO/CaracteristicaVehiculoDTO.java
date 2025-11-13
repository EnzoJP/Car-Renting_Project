package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaracteristicaVehiculoDTO implements Identifiable<Long> {
    private Long id;
    private String marca;
    private String modelo;
    private int cantidadPuerta;
    private int cantidadAsiento;
    private long anio;
    private int cantidadTotalVehiculo;
    private int cantidadVehiculoAlquilado;
    private ImagenDTO imagen;
}
