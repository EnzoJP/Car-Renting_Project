package com.servidor.servidor.services;

import com.servidor.servidor.entities.CaracteristicaVehiculo;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.CaracteristicaVehiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class CaracteristicaVehiculoService extends BaseService<CaracteristicaVehiculo, Long> {

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
}
