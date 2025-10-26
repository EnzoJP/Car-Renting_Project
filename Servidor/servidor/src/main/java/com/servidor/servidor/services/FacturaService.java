package com.servidor.servidor.services;

import com.servidor.servidor.entities.Factura;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.FacturaRepository;
import org.springframework.stereotype.Service;

@Service
public class FacturaService extends BaseService<Factura, Long> {

    public FacturaService(FacturaRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(Factura factura) throws ErrorServiceException {
        try {
            if (factura.getNumeroFactura() <= 0) {
                throw new ErrorServiceException("El número de factura debe ser mayor a cero");
            }
            if (factura.getFechaFactura() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de la factura");
            }
            if (factura.getTotalPagado() < 0) {
                throw new ErrorServiceException("El total pagado no puede ser negativo");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
