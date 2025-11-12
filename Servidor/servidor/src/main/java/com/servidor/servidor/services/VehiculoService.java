package com.servidor.servidor.services;

import com.servidor.servidor.entities.Vehiculo;
import com.servidor.servidor.enums.EstadoVehiculo;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VehiculoService extends BaseService<Vehiculo, Long> {

    @Autowired
    private CaracteristicaVehiculoService caracteristicaVehiculoService;

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository repository) {
        super(repository);
        this.vehiculoRepository = repository;
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

    @Override
    @Transactional
    public Vehiculo alta(Vehiculo vehiculo) throws ErrorServiceException {
        Vehiculo guardado = super.alta(vehiculo);

        // Recalcular las cantidades de la característica asociada
        if (guardado.getCaracteristicaVehiculo() != null) {
            caracteristicaVehiculoService.recalcularCantidades(
                    guardado.getCaracteristicaVehiculo().getId()
            );
        }

        return guardado;
    }

    @Override
    @Transactional
    public Optional<Vehiculo> modificar(Long id, Vehiculo vehiculo) throws ErrorServiceException {
        Optional<Vehiculo> modificado = super.modificar(id, vehiculo);

        if (modificado.isPresent() && modificado.get().getCaracteristicaVehiculo() != null) {
            caracteristicaVehiculoService.recalcularCantidades(
                    modificado.get().getCaracteristicaVehiculo().getId()
            );
        }

        return modificado;
    }

    /**
     * Sobrescribe bajaLogica para recalcular cantidades al eliminar un vehículo
     */
    @Override
    @Transactional
    public boolean bajaLogica(Long id) throws ErrorServiceException {
        Vehiculo vehiculo = repository.findById(id)
                .orElseThrow(() -> new ErrorServiceException("Vehículo no encontrado"));

        Long caracteristicaId = vehiculo.getCaracteristicaVehiculo() != null
                ? vehiculo.getCaracteristicaVehiculo().getId()
                : null;

        boolean eliminado = super.bajaLogica(id);

        //recalcular las cantidades de la característica asociada
        if (eliminado && caracteristicaId != null) {
            caracteristicaVehiculoService.recalcularCantidades(caracteristicaId);
        }

        return eliminado;
    }


    @Transactional
    public Vehiculo cambiarEstado(Long id, String nuevoEstado) throws ErrorServiceException {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ErrorServiceException("Vehículo no encontrado con ID: " + id));
        try {
            EstadoVehiculo estadoEnum = EstadoVehiculo.valueOf(nuevoEstado.toUpperCase());
            vehiculo.setEstadoVehiculo(estadoEnum);
            return vehiculoRepository.save(vehiculo);
        } catch (IllegalArgumentException e) {
            throw new ErrorServiceException("El estado '" + nuevoEstado + "' no es válido.");
        }
    }
}