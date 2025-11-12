package com.servidor.servidor.services;

import com.servidor.servidor.entities.Alquiler;
import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.AlquilerRepository;
import com.servidor.servidor.repositories.UsuarioClienteRepository;
import com.servidor.servidor.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService extends BaseService<Alquiler, Long> {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private CaracteristicaVehiculoService caracteristicaVehiculoService;

    private final AlquilerRepository alquilerRepository;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioClienteRepository usuarioClienteRepository;

    public AlquilerService(AlquilerRepository repository, EmailService emailService,
                           UsuarioRepository usuarioRepository, UsuarioClienteRepository usuarioClienteRepository) {
        super(repository);
        this.alquilerRepository = repository;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioClienteRepository = usuarioClienteRepository;
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

    @Override
    @Transactional
    public Alquiler alta(Alquiler alquiler) throws ErrorServiceException {
        Alquiler guardado = super.alta(alquiler);

        //recalcular las cantidades de la característica asociada al auto alquilado
        if (guardado.getVehiculo() != null
                && guardado.getVehiculo().getCaracteristicaVehiculo() != null) {
            caracteristicaVehiculoService.recalcularCantidades(
                    guardado.getVehiculo().getCaracteristicaVehiculo().getId()
            );
        }
        return guardado;
    }

    @Override
    @Transactional
    public Optional<Alquiler> modificar(Long id, Alquiler alquiler) throws ErrorServiceException {
        Optional<Alquiler> modificado = super.modificar(id, alquiler);

        //recalcular las cantidades de la caracterisitca asociada
        if (modificado.isPresent()
                && modificado.get().getVehiculo() != null
                && modificado.get().getVehiculo().getCaracteristicaVehiculo() != null) {
            caracteristicaVehiculoService.recalcularCantidades(
                    modificado.get().getVehiculo().getCaracteristicaVehiculo().getId()
            );
        }

        return modificado;
    }

    @Override
    @Transactional
    public boolean bajaLogica(Long id) throws ErrorServiceException {
        //obtener el alquiler antes de eliminarlo
        Alquiler alquiler = repository.findById(id)
                .orElseThrow(() -> new ErrorServiceException("Alquiler no encontrado"));

        Long caracteristicaId = null;
        if (alquiler.getVehiculo() != null
                && alquiler.getVehiculo().getCaracteristicaVehiculo() != null) {
            caracteristicaId = alquiler.getVehiculo().getCaracteristicaVehiculo().getId();
        }

        boolean eliminado = super.bajaLogica(id);

        //recalcular las cantidades
        if (eliminado && caracteristicaId != null) {
            caracteristicaVehiculoService.recalcularCantidades(caracteristicaId);
        }
        return eliminado;
    }

    // para el dashboard cliente
    public List<Alquiler> obtenerAlquileresPorCliente(Long idCliente) {
        return alquilerRepository.findByClienteId(idCliente);
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 46 12 * * ?")
    public void enviarRecordatoriosDeDevolucion() {
        LocalDate manana = LocalDate.now().plusDays(1);
        System.out.println("--- Ejecutando tarea: Recordatorios de Devolución para " + manana + " ---");

        List<Alquiler> alquileres = alquilerRepository.findAlquileresParaDevolverManana(manana);

        if (alquileres.isEmpty()) {
            System.out.println("No hay alquileres para recordar hoy.");
            return;
        }

        System.out.println("Se encontraron " + alquileres.size() + " alquileres para recordar.");

        for (Alquiler alquiler : alquileres) {
            try {
                // datos del Cliente
                Cliente cliente = alquiler.getCliente();
                if (cliente == null) continue;
                String nombreCliente = cliente.getNombre() + " " + cliente.getApellido();
                // datos del Vehículo
                String vehiculoStr = "Vehículo no especificado";
                if (alquiler.getVehiculo() != null && alquiler.getVehiculo().getCaracteristicaVehiculo() != null) {
                    vehiculoStr = alquiler.getVehiculo().getCaracteristicaVehiculo().getMarca() + " " +
                            alquiler.getVehiculo().getCaracteristicaVehiculo().getModelo();
                }
                //Fecha de Devolución
                LocalDate fechaDevolucion = alquiler.getFechaHasta().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                //Obtener Email del Cliente
                String emailDestinatario = usuarioClienteRepository.findByClienteId(cliente.getId())
                        .flatMap(relacion -> usuarioRepository.findById(relacion.getUsuario().getId()))
                        .map(Usuario::getUsername)
                        .orElse(null);
                if (emailDestinatario == null) {
                    System.err.println("No se pudo encontrar email para el cliente ID: " + cliente.getId());
                    continue;
                }
                //Enviar el Email
                emailService.enviarRecordatorioDevolucion(
                        emailDestinatario,
                        nombreCliente,
                        vehiculoStr,
                        fechaDevolucion
                );
            } catch (Exception e) {
                System.err.println("Error al procesar recordatorio para alquiler ID: " + alquiler.getId());
                e.printStackTrace();
            }
        }
    }
}
