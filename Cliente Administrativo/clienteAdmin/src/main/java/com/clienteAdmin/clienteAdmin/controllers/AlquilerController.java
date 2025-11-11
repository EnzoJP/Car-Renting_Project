package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.AlquilerDTO;
import com.clienteAdmin.clienteAdmin.DTO.CostoVehiculoDTO;
import com.clienteAdmin.clienteAdmin.DTO.DocumentacionDTO;
import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.AlquilerService;
import com.clienteAdmin.clienteAdmin.services.ClienteService;
import com.clienteAdmin.clienteAdmin.services.CostoVehiculoService;
import com.clienteAdmin.clienteAdmin.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/alquiler")
public class AlquilerController extends BaseController<AlquilerDTO, Long> {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private CostoVehiculoService costoVehiculoService;

    public AlquilerController(AlquilerService service) {
        super(service);
        AlquilerDTO defaultDto = new AlquilerDTO();
        defaultDto.setEstadoAlquiler("ADEUDADO");
        initController(defaultDto,
                "Listado de Alquileres",
                "Gestión de Alquiler",
                "view/admin/Menu/alquileres/");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        this.model.addAttribute("clientes", clienteService.listarActivos());

        this.model.addAttribute("vehiculos",
                vehiculoService.listarActivos().stream()
                        .filter(v -> "DISPONIBLE".equals(v.getEstadoVehiculo()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        preAlta();
    }

    // Helper para normalizar strings (marca/modelo)
    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    // un solo archivo contiene toda la documentación necesaria
    @Override
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("item") AlquilerDTO entidad,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes attributes,
                             Model model) {
        try {
            this.model = model;
            preActualziacion();

            if (file != null && !file.isEmpty()) {
                DocumentacionDTO doc = new DocumentacionDTO();
                doc.setNombreArchivo(file.getOriginalFilename());
                // Aquí puedes setear el tipo si lo necesitas, ej:
                // doc.setTipoDocumentacion("ALQUILER_DOCS");
                entidad.setDocumentacion(doc);
            }

            if (entidad.getId() == null) {
                // Cargar el vehículo completo si solo tiene id
                if (entidad.getVehiculo() != null && entidad.getVehiculo().getId() != null) {
                    VehiculoDTO veh = vehiculoService.obtener(entidad.getVehiculo().getId());
                    entidad.setVehiculo(veh);
                }

                // Calcular costo
                if (entidad.getVehiculo() != null && entidad.getVehiculo().getCaracteristicaVehiculo() != null
                        && entidad.getFechaDesde() != null && entidad.getFechaHasta() != null) {
                    long dias = ChronoUnit.DAYS.between(
                        entidad.getFechaDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        entidad.getFechaHasta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    );

                    // preparar datos para comparación
                    Long caracteristicaId = entidad.getVehiculo().getCaracteristicaVehiculo().getId();
                    String marcaNorm = normalize(entidad.getVehiculo().getCaracteristicaVehiculo().getMarca());
                    String modeloNorm = normalize(entidad.getVehiculo().getCaracteristicaVehiculo().getModelo());

                    double costoDiario = costoVehiculoService.listarActivos().stream()
                        .filter(cv -> {
                            if (cv.getCaracteristicaVehiculo() == null) return false;
                            // si existe id en ambas, compararlo
                            if (caracteristicaId != null && cv.getCaracteristicaVehiculo().getId() != null) {
                                return Objects.equals(cv.getCaracteristicaVehiculo().getId(), caracteristicaId);
                            }
                            // fallback: comparar marca+modelo normalizados
                            String cvMarca = normalize(cv.getCaracteristicaVehiculo().getMarca());
                            String cvModelo = normalize(cv.getCaracteristicaVehiculo().getModelo());
                            return cvMarca.equals(marcaNorm) && cvModelo.equals(modeloNorm);
                        })
                        .findFirst()
                        .map(CostoVehiculoDTO::getCosto)
                        .orElse(0.0);
                    entidad.setCosto(dias * costoDiario);
                } else {
                    entidad.setCosto(0.0); // O manejar el error mostrando mensaje
                }

                service.alta(entidad);
                if (entidad.getVehiculo() != null && entidad.getVehiculo().getId() != null) {
                    vehiculoService.cambiarEstado(entidad.getVehiculo().getId(), "ALQUILADO");
                }

            } else {
                service.modificar(entidad.getId(), entidad);
            }

            postActualziacion();
            attributes.addFlashAttribute("msgExito", "Alquiler guardado con éxito.");
            return redirectList;

        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            try { preAlta(); } catch (Exception ex) { /* Ignorar error de recarga */ }
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema: " + e.getMessage());
            try { preAlta(); } catch (Exception ex) { /* Ignorar error de recarga */ }
            return viewEdit;
        }
    }
}