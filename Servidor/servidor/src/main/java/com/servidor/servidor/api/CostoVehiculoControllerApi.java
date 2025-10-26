package com.servidor.servidor.api;

import com.servidor.servidor.entities.CostoVehiculo;
import com.servidor.servidor.services.CostoVehiculoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/costovehiculos")
public class CostoVehiculoControllerApi extends BaseControllerApi<CostoVehiculo, Long> {

    public CostoVehiculoControllerApi(CostoVehiculoService service) {
        super(service);
    }
}
