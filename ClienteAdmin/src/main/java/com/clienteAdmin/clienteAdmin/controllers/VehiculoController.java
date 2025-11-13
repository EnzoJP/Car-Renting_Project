package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.VehiculoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.CaracteristicaVehiculoService; // Importante
import com.clienteAdmin.clienteAdmin.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/vehiculo")
public class VehiculoController extends BaseController<VehiculoDTO, Long> {

    @Autowired
    private CaracteristicaVehiculoService caracteristicaVehiculoService;

    public VehiculoController(VehiculoService service) {
        super(service);
        initController(new VehiculoDTO(),
                "Listado de Flota (Vehículos)",
                "Gestión de Vehículo",
                "view/admin/Menu/vehiculos/");
    }

    @GetMapping("/menu")
    public String mostrarMenuVehiculos(Model model) {
        model.addAttribute("titulo", "Gestión de Vehículos");

        // Apunta al archivo HTML que creamos arriba
        return "view/admin/Menu/vehiculos/detalle";
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        this.model.addAttribute("caracteristicas", caracteristicaVehiculoService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        preAlta();
    }
}