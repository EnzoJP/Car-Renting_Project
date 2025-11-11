package com.servidor.servidor.repositories;

import com.servidor.servidor.entities.CaracteristicaVehiculo;
import com.servidor.servidor.entities.Vehiculo;

import java.util.List;

public interface VehiculoRepository extends BaseRepository<Vehiculo, Long> {

    List<Vehiculo> findByCaracteristicaVehiculoAndEliminadoFalse(CaracteristicaVehiculo caracteristica);
}
