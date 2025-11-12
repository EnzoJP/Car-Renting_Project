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

import java.util.Optional;

@Service
public class VehiculoService extends BaseService<Vehiculo, Long> {

    @Autowired
    private CaracteristicaVehiculoService caracteristicaVehiculoService;

    @Autowired
    private CaracteristicaVehiculoRepository caracteristicaVehiculoRepository;

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
        Optional<Vehiculo> optVehiculo = vehiculoRepository.findById(id);
        if (optVehiculo.isEmpty()) {
            throw new ErrorServiceException("No se encontró el vehículo con ID " + id);
        }

        Vehiculo vehiculo = optVehiculo.get();
        CaracteristicaVehiculo caracteristica = vehiculo.getCaracteristicaVehiculo();

        if (caracteristica == null) {
            throw new ErrorServiceException("El vehículo no tiene características asociadas");
        }

        EstadoVehiculo estadoAnterior = vehiculo.getEstadoVehiculo();

        // Actualizar el estado
        vehiculo.setEstadoVehiculo(EstadoVehiculo.valueOf(nuevoEstado.toUpperCase()));

        // Actualizar los contadores de stock
        if (!nuevoEstado.equalsIgnoreCase(estadoAnterior.name())) {
            if (nuevoEstado.equalsIgnoreCase("ALQUILADO")) {
                caracteristica.setCantidadVehiculoAlquilado(
                        caracteristica.getCantidadVehiculoAlquilado() + 1
                );
            } else if (estadoAnterior == EstadoVehiculo.ALQUILADO
                    && nuevoEstado.equalsIgnoreCase("DISPONIBLE")
                    && caracteristica.getCantidadVehiculoAlquilado() > 0) {
                caracteristica.setCantidadVehiculoAlquilado(
                        caracteristica.getCantidadVehiculoAlquilado() - 1
                );
            }
        }

        caracteristicaVehiculoRepository.save(caracteristica);
        vehiculoRepository.save(vehiculo);

        return vehiculo;
    }
}