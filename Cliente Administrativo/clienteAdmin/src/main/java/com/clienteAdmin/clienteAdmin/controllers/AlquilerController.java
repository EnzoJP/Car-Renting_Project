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

import java.time.LocalDate; // Importar
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/alquiler")
public class AlquilerController extends BaseController<AlquilerDTO, Long> {

    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;
    private final CostoVehiculoService costoVehiculoService;
    private final AlquilerService alquilerService;

    @Autowired
    public AlquilerController(AlquilerService service, ClienteService clienteService,
                              VehiculoService vehiculoService, CostoVehiculoService costoVehiculoService) {
        super(service);
        this.alquilerService = service;
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
        this.costoVehiculoService = costoVehiculoService;

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

        //obtener solo autos DISPONIBLES que dado una marca y un modelo,tenga stcok
        List<VehiculoDTO> vehiculosDisponibles = vehiculoService.listarActivos().stream()
                .filter(v -> "DISPONIBLE".equals(v.getEstadoVehiculo()))
                .filter(v -> {
                    if (v.getCaracteristicaVehiculo() != null) {
                        int total = v.getCaracteristicaVehiculo().getCantidadTotalVehiculo();
                        int alquilados = v.getCaracteristicaVehiculo().getCantidadVehiculoAlquilado();
                        int disponibles = total - alquilados;

                        //solo mostrar si hay al menos 1 disponible
                        return disponibles > 0;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        this.model.addAttribute("vehiculos", vehiculosDisponibles);
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        preAlta();
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    @Override
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("item") AlquilerDTO entidad,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes attributes,
                             Model model) {
        try {
            this.model = model;
            preActualziacion();

            // === CÁLCULO DE COSTO (solo para altas) ===
            if (entidad.getId() == null) {
                if (entidad.getVehiculo() != null && entidad.getVehiculo().getId() != null) {
                    VehiculoDTO veh = vehiculoService.obtener(entidad.getVehiculo().getId());
                    entidad.setVehiculo(veh);
                }

                if (entidad.getVehiculo() != null && entidad.getVehiculo().getCaracteristicaVehiculo() != null
                        && entidad.getFechaDesde() != null && entidad.getFechaHasta() != null) {

                    LocalDate desde = entidad.getFechaDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate hasta = entidad.getFechaHasta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long dias = ChronoUnit.DAYS.between(desde, hasta);
                    if (dias < 1) dias = 1;

                    Long caracteristicaId = entidad.getVehiculo().getCaracteristicaVehiculo().getId();
                    String marcaNorm = normalize(entidad.getVehiculo().getCaracteristicaVehiculo().getMarca());
                    String modeloNorm = normalize(entidad.getVehiculo().getCaracteristicaVehiculo().getModelo());

                    double costoDiario = costoVehiculoService.listarActivos().stream()
                            .filter(cv -> {
                                if (cv.getCaracteristicaVehiculo() == null) return false;
                                if (caracteristicaId != null && cv.getCaracteristicaVehiculo().getId() != null) {
                                    return Objects.equals(cv.getCaracteristicaVehiculo().getId(), caracteristicaId);
                                }
                                String cvMarca = normalize(cv.getCaracteristicaVehiculo().getMarca());
                                String cvModelo = normalize(cv.getCaracteristicaVehiculo().getModelo());
                                return cvMarca.equals(marcaNorm) && cvModelo.equals(modeloNorm);
                            })
                            .findFirst()
                            .map(CostoVehiculoDTO::getCosto)
                            .orElse(0.0);

                    entidad.setCosto(dias * costoDiario);
                } else {
                    entidad.setCosto(0.0);
                }
            }

            // === PROCESAMIENTO DEL ARCHIVO ===
            byte[] fileBytes = null;
            String originalFileName = null;

            if (file != null && !file.isEmpty()) {
                fileBytes = file.getBytes();
                originalFileName = file.getOriginalFilename();

                DocumentacionDTO doc = new DocumentacionDTO();
                doc.setTipoDocumentacion("CARNET_DE_CONDUCIR");
                entidad.setDocumentacion(doc);

            } else if (entidad.getId() == null) {
                throw new ErrorServiceException("Debe adjuntar un archivo de documentación para un nuevo alquiler.");
            }

            // === GUARDAR ALQUILER ===
            System.out.println("=== Guardando alquiler ===");
            System.out.println("ID: " + entidad.getId());
            System.out.println("Cliente ID: " + (entidad.getCliente() != null ? entidad.getCliente().getId() : "null"));
            System.out.println("Vehículo ID: " + (entidad.getVehiculo() != null ? entidad.getVehiculo().getId() : "null"));

            AlquilerDTO alquilerGuardado = alquilerService.saveWithFile(entidad, fileBytes, originalFileName);

            System.out.println("Alquiler guardado exitosamente con ID: " + alquilerGuardado.getId());

            // === CAMBIAR ESTADO DEL VEHÍCULO (solo para altas y si está pagado) ===
            if (entidad.getId() == null
                    && "PAGADO".equals(entidad.getEstadoAlquiler())  // ← AGREGADO
                    && entidad.getVehiculo() != null
                    && entidad.getVehiculo().getId() != null) {
                try {
                    System.out.println("Cambiando estado del vehículo " + entidad.getVehiculo().getId() + " a ALQUILADO");
                    vehiculoService.cambiarEstado(entidad.getVehiculo().getId(), "ALQUILADO");
                    System.out.println("Estado del vehículo cambiado exitosamente");
                } catch (Exception e) {
                    System.err.println("Advertencia: No se pudo cambiar el estado del vehículo: " + e.getMessage());
                    e.printStackTrace();
                    // alquiler guardado sin excepcion
                }
            }

            postActualziacion();
            attributes.addFlashAttribute("msgExito", "Alquiler guardado con éxito.");
            return redirectList;

        } catch (ErrorServiceException e) {
            System.err.println("Error de validación: " + e.getMessage());
            model.addAttribute("msgError", e.getMessage());
            model.addAttribute("isDisabled", false);
            try { preAlta(); } catch (Exception ex) { }
            return viewEdit;
        } catch (Exception e) {
            System.err.println("Error inesperado en actualizar alquiler:");
            e.printStackTrace();
            model.addAttribute("msgError", "Error de Sistema: " + e.getMessage());
            model.addAttribute("isDisabled", false);
            try { preAlta(); } catch (Exception ex) { }
            return viewEdit;
        }
    }
}