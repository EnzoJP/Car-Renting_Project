package com.servidor.servidor.api;

import com.servidor.servidor.entities.Vehiculo;
import com.servidor.servidor.services.VehiculoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/vehiculos")
public class VehiculoControllerApi extends BaseControllerApi<Vehiculo, Long> {

    public VehiculoControllerApi(VehiculoService service) {
        super(service);
    }
}
