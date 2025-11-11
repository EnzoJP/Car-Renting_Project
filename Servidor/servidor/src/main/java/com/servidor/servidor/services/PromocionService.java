package com.servidor.servidor.services;

import com.servidor.servidor.entities.Promocion;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.PromocionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PromocionService extends BaseService<Promocion, Long> {

    // Ya no inyectamos ClienteRepository
    public PromocionService(PromocionRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Promocion promocion) throws ErrorServiceException {
        try {
            if (promocion.getPorcentajeDescuento() <= 0 || promocion.getPorcentajeDescuento() > 100) {
                throw new ErrorServiceException("El porcentaje de descuento debe ser mayor a 0 y menor o igual a 100.");
            }
            if (promocion.getCodigoDescuento() == null || promocion.getCodigoDescuento().trim().isEmpty()) {
                throw new ErrorServiceException("El código de descuento no puede estar vacío.");
            }
            if (promocion.getFechaInicioPromocion() == null) {
                throw new ErrorServiceException("La fecha de inicio es obligatoria.");
            }
            if (promocion.getFechaFinPromocion() == null) {
                throw new ErrorServiceException("La fecha de fin es obligatoria.");
            }
            if (promocion.getFechaInicioPromocion().after(promocion.getFechaFinPromocion())) {
                throw new ErrorServiceException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error del sistema al validar la promoción.");
        }
    }
}