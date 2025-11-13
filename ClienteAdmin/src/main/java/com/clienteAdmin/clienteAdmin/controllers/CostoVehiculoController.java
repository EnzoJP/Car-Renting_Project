package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.CostoVehiculoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.CaracteristicaVehiculoService; // Importante
import com.clienteAdmin.clienteAdmin.services.CostoVehiculoService; // El servicio de este DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/costo_vehiculo")
// X DIA ES EL CSOTO VEHICULO
public class CostoVehiculoController extends BaseController<CostoVehiculoDTO, Long> {

    @Autowired
    private CaracteristicaVehiculoService caracteristicaVehiculoService;

    public CostoVehiculoController(CostoVehiculoService service) {
        super(service);
        initController(new CostoVehiculoDTO(),
                "Listado de Tarifas por Día",
                "Gestión de Tarifa",
                "view/admin/Menu/vehiculos/");
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