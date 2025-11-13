package com.servidor.servidor.services;

import com.servidor.servidor.entities.CostoVehiculo;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.CostoVehiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class CostoVehiculoService extends BaseService<CostoVehiculo, Long> {

    public CostoVehiculoService(CostoVehiculoRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(CostoVehiculo costoVehiculo) throws ErrorServiceException {
        try {
            if (costoVehiculo.getFechaDesde() == null) {
                throw new ErrorServiceException("Debe indicar la fecha desde del costo");
            }
            if (costoVehiculo.getFechaHasta() == null) {
                throw new ErrorServiceException("Debe indicar la fecha hasta del costo");
            }
            if (costoVehiculo.getCosto() <= 0) {
                throw new ErrorServiceException("El costo debe ser mayor a cero");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
