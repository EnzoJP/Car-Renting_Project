package com.servidor.servidor.services;

import com.servidor.servidor.entities.CaracteristicaVehiculo;
import com.servidor.servidor.entities.Vehiculo;
import com.servidor.servidor.enums.EstadoVehiculo;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.CaracteristicaVehiculoRepository;
import com.servidor.servidor.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaracteristicaVehiculoService extends BaseService<CaracteristicaVehiculo, Long> {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public CaracteristicaVehiculoService(CaracteristicaVehiculoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(CaracteristicaVehiculo caracteristica) throws ErrorServiceException {
        try {
            if (caracteristica.getMarca() == null || caracteristica.getMarca().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la marca del vehículo");
            }
            if (caracteristica.getModelo() == null || caracteristica.getModelo().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el modelo del vehículo");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    /**
     * Recalcula las cantidades de vehículos para una característica específica
     * contando los vehículos físicos asociados en la base de datos
     */
    @Transactional
    public void recalcularCantidades(Long caracteristicaId) {
        try {
            CaracteristicaVehiculo caracteristica = repository.findById(caracteristicaId)
                    .orElseThrow(() -> new ErrorServiceException("Característica no encontrada"));

            //contar TODOS los vehículos de esta característica
            List<Vehiculo> todosLosVehiculos = vehiculoRepository
                    .findByCaracteristicaVehiculoAndEliminadoFalse(caracteristica);

            int cantidadTotal = todosLosVehiculos.size();

            // Contar solo los vehículos ALQUILADOS
            long cantidadAlquilados = todosLosVehiculos.stream()
                    .filter(v -> v.getEstadoVehiculo() == EstadoVehiculo.ALQUILADO)
                    .count();

            // Actualizar los campos
            caracteristica.setCantidadTotalVehiculo(cantidadTotal);
            caracteristica.setCantidadVehiculoAlquilado((int) cantidadAlquilados);

            repository.save(caracteristica);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Recalcula las cantidades de TODAS las características
     */
    @Transactional
    public void recalcularTodasLasCantidades() {
        List<CaracteristicaVehiculo> todas = listarActivos();
        for (CaracteristicaVehiculo caracteristica : todas) {
            recalcularCantidades(caracteristica.getId());
        }
    }
}