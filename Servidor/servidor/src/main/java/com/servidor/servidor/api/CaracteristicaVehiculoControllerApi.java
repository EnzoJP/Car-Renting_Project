package com.servidor.servidor.api;

import com.servidor.servidor.entities.CaracteristicaVehiculo;
import com.servidor.servidor.services.CaracteristicaVehiculoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/caracteristicavehiculos")
public class CaracteristicaVehiculoControllerApi extends BaseControllerApi<CaracteristicaVehiculo, Long> {

    public CaracteristicaVehiculoControllerApi(CaracteristicaVehiculoService service) {
        super(service);
    }
}
