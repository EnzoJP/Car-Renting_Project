package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.CaracteristicaVehiculoDTO;
import com.clienteAdmin.clienteAdmin.DTO.ImagenDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.CaracteristicaVehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/caractvehiculo")
public class CaracteristicaVehiculoController extends BaseController<CaracteristicaVehiculoDTO, Long> {

    public CaracteristicaVehiculoController(CaracteristicaVehiculoService service) {
        super(service);
        initController(new CaracteristicaVehiculoDTO(),
                "Listado de Modelos de Vehículos",
                "Gestión de Modelo de Vehículo",
                "view/admin/Menu/vehiculos/");
    }


    @Override
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("item") CaracteristicaVehiculoDTO entidad,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes attributes,
                             Model model) {
        try {
            this.model = model;
            preActualziacion();

            // logica imagen cono en cliente
            if (file != null && !file.isEmpty()) {
                ImagenDTO imagen = new ImagenDTO();
                imagen.setContenido(file.getBytes());
                imagen.setMime(file.getContentType());
                entidad.setImagen(imagen);
            }

            if (entidad.getId() == null) {
                service.alta(entidad);
            } else {
                service.modificar(entidad.getId(), entidad);
            }

            postActualziacion();
            attributes.addFlashAttribute("msgExito", "Modelo de vehículo guardado con éxito.");
            return redirectList;

        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema: " + e.getMessage());
            return viewEdit;
        }
    }

}
