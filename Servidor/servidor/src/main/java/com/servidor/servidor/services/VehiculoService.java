package com.servidor.servidor.services;

import com.servidor.servidor.entities.Vehiculo;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService extends BaseService<Vehiculo, Long> {

    public VehiculoService(VehiculoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Vehiculo vehiculo) throws ErrorServiceException {
        try {
            if (vehiculo.getPatente() == null || vehiculo.getPatente().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la patente del vehículo");
            }
            if (vehiculo.getCaracteristicaVehiculo() == null) {
                throw new ErrorServiceException("Debe indicar las características del vehículo");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
