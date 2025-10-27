package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculoDTO implements Identifiable<Long> {
    private Long id;
    private String estadoVehiculo;
    private String patente;
    private CaracteristicaVehiculoDTO caracteristicaVehiculo;
}
