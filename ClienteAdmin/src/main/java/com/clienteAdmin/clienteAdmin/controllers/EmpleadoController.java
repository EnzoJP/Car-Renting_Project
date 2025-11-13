package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.EmpleadoDTO;
import com.clienteAdmin.clienteAdmin.DTO.DireccionDTO;
import com.clienteAdmin.clienteAdmin.DTO.LocalidadDTO;
import com.clienteAdmin.clienteAdmin.DTO.ImagenDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/empleado")
public class EmpleadoController extends BaseController<EmpleadoDTO, Long> {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private PaisService paisService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private LocalidadService localidadService;

    public EmpleadoController(EmpleadoService service) {
        super(service);
        initController(new EmpleadoDTO(),
                "LIST EMPLEADOS",
                "EDIT EMPLEADOS",
                "view/admin/Menu/usuarios/");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());

        // Cargar datos para la cascada de dirección
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
        model.addAttribute("departamentos", departamentoService.listarActivos());
        model.addAttribute("localidades", localidadService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        model.addAttribute("imagenes", imagenService.listarActivos());
        model.addAttribute("contactos", contactoService.listarActivos());
        model.addAttribute("direcciones", direccionService.listarActivos());

        // Cargar datos para la cascada de dirección
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
        model.addAttribute("departamentos", departamentoService.listarActivos());
        model.addAttribute("localidades", localidadService.listarActivos());
    }


    @PostMapping("/actualizarEmpleado")
    public String actualizar(
            @ModelAttribute("item") EmpleadoDTO entidad,
            @RequestParam(value = "file", required = false) MultipartFile file,
            // Parámetros de dirección
            @RequestParam(value = "localidadId", required = false) Long localidadId,
            @RequestParam(value = "calle", required = false) String calle,
            @RequestParam(value = "numeracion", required = false) String numeracion,
            @RequestParam(value = "barrio", required = false) String barrio,
            @RequestParam(value = "manzanaPiso", required = false) String manzanaPiso,
            @RequestParam(value = "casaDepartamento", required = false) String casaDepartamento,
            @RequestParam(value = "referencia", required = false) String referencia,
            RedirectAttributes attributes,
            Model model) {
        try {
            this.model = model;
            preActualziacion();

            // Procesar imagen si existe
            if (file != null && !file.isEmpty()) {
                ImagenDTO imagen = new ImagenDTO();
                imagen.setContenido(file.getBytes());
                imagen.setMime(file.getContentType());

                // guardar la imagen antes de asignarla al empleado
                ImagenDTO imagenGuardada = imagenService.alta(imagen);
                entidad.setImagen(imagenGuardada);
            }

            // Construir la dirección manualmente
            if (localidadId != null && calle != null && !calle.trim().isEmpty()) {
                DireccionDTO direccion = new DireccionDTO();
                direccion.setCalle(calle);
                direccion.setNumeracion(numeracion);
                direccion.setBarrio(barrio);
                direccion.setManzanaPiso(manzanaPiso);
                direccion.setCasaDepartamento(casaDepartamento);
                direccion.setReferencia(referencia);

                // Vincular con la localidad
                LocalidadDTO localidad = new LocalidadDTO();
                localidad.setId(localidadId);
                direccion.setLocalidad(localidad);

                // Guardar la dirección primero
                DireccionDTO direccionGuardada = direccionService.alta(direccion);
                entidad.setDireccion(direccionGuardada);
            }

            // Guardar el empleado
            if (entidad.getId() == null) {
                service.alta(entidad);
            } else {
                service.modificar(entidad.getId(), entidad);
            }

            postActualziacion();
            attributes.addFlashAttribute("msgExito", "La acción fue realizada correctamente.");
            return redirectList;

        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            model.addAttribute("isDisabled", false);
            model.addAttribute("item", entidad);
            try {
                preModificacion();
            } catch (ErrorServiceException ex) {
                model.addAttribute("msgError", "Error de Sistema al recargar el formulario.");
            }
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema: " + e.getMessage());
            model.addAttribute("isDisabled", false);
            model.addAttribute("item", entidad);
            try {
                preModificacion();
            } catch (ErrorServiceException ex) {
                model.addAttribute("msgError", "Error de Sistema al recargar el formulario.");
            }
            return viewEdit;
        }
    }
}