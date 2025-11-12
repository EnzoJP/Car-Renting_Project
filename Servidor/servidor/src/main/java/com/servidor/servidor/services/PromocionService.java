package com.servidor.servidor.services;

import com.servidor.servidor.entities.Promocion;
import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.enums.RolUsuario;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.repositories.PromocionRepository;
import com.servidor.servidor.repositories.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromocionService extends BaseService<Promocion, Long> {

    private final PromocionRepository promocionRepository;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;

    public PromocionService(PromocionRepository repository, EmailService emailService, UsuarioRepository usuarioRepository) {
        super(repository);
        this.promocionRepository = repository;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
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

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 8 * * ?") // Todos los días a las 8 AM
    public void enviarCorreosPromocionesNuevas() {
        LocalDate hoy = LocalDate.now();
        List<Promocion> promocionesDeHoy = promocionRepository.findPromocionesQueInicianHoy(hoy);
        if (promocionesDeHoy.isEmpty()) {
            return;
        }
        //Obtener la lista de emails de todos los clientes
        List<String> emailsClientes = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == RolUsuario.CLIENTE && u.getEliminado() == false)
                .map(Usuario::getUsername) // Asumiendo que username es el email
                .collect(Collectors.toList());
        if (emailsClientes.isEmpty()) {
            System.out.println("No se encontraron clientes activos para enviar correos.");
            return;
        }
        //enviar correos por cada promoción
        for (Promocion promo : promocionesDeHoy) {
            try {
                String asunto = String.format("¡Nueva Promoción! %.0f%% OFF con el código %s",
                        promo.getPorcentajeDescuento(), promo.getCodigoDescuento());
                // Formatear la fecha de fin
                String fechaFinFormateada = promo.getFechaFinPromocion().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String cuerpoHtml = emailService.crearHtmlPromocion(
                        promo.getDescripcionDescuento(),
                        promo.getCodigoDescuento(),
                        promo.getPorcentajeDescuento(),
                        fechaFinFormateada
                );
                System.out.println("Enviando promoción '" + promo.getCodigoDescuento() + "' a " + emailsClientes.size() + " clientes.");
                emailService.enviarEmailMasivoBCC(emailsClientes, asunto, cuerpoHtml);
            } catch (Exception e) {
                System.err.println("Error al procesar o enviar correos para la promoción: " + promo.getId());
                e.printStackTrace();
            }
        }
    }
}