package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.AlquilerDTO;
import com.clienteAdmin.clienteAdmin.DTO.DocumentacionDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.AlquilerService;
import com.clienteAdmin.clienteAdmin.services.ClienteService;
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

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/alquiler")
public class AlquilerController extends BaseController<AlquilerDTO, Long> {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VehiculoService vehiculoService;

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService service) {
        super(service);
        this.alquilerService = service;
        initController(new AlquilerDTO(),
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