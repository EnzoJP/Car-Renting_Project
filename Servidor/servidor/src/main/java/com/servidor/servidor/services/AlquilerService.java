package com.servidor.servidor.services;

import com.servidor.servidor.entities.Alquiler;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.AlquilerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlquilerService extends BaseService<Alquiler, Long> {

    private final AlquilerRepository alquilerRepository;

    public AlquilerService(AlquilerRepository repository) {
        super(repository);
        this.alquilerRepository = repository;
    }

    @Override
    protected void validar(Alquiler alquiler) throws ErrorServiceException {
        try {
            if (alquiler.getFechaDesde() == null) {
                throw new ErrorServiceException("Debe indicar la fecha desde del alquiler");
            }
            if (alquiler.getFechaHasta() == null) {
                throw new ErrorServiceException("Debe indicar la fecha hasta del alquiler");
            }
            if (alquiler.getCliente() == null) {
                throw new ErrorServiceException("Debe indicar el cliente del alquiler");
            }
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    // para el dashboard cliente
    public List<Alquiler> obtenerAlquileresPorCliente(Long idCliente) {
        return alquilerRepository.findByClienteId(idCliente);
    }
}
