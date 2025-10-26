package com.servidor.servidor.services;

import com.servidor.servidor.entities.DetalleFactura;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.DetalleFacturaRepository;
import org.springframework.stereotype.Service;

@Service
public class DetalleFacturaService extends BaseService<DetalleFactura, Long> {

    public DetalleFacturaService(DetalleFacturaRepository repository) {
        super(repository);
    }

    @Override
    protected void validar(DetalleFactura detalleFactura) throws ErrorServiceException {
        try {
            if (detalleFactura.getCantidad() <= 0) {
                throw new ErrorServiceException("La cantidad debe ser mayor a cero");
            }
            if (detalleFactura.getSubtotal() <= 0) {
                throw new ErrorServiceException("El subtotal debe ser mayor a cero");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
}
