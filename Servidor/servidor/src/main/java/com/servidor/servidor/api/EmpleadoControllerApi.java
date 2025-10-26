package com.servidor.servidor.api;

import com.servidor.servidor.entities.Empleado;
import com.servidor.servidor.services.EmpleadoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/empleados")
public class EmpleadoControllerApi extends BaseControllerApi<Empleado, Long> {

    public EmpleadoControllerApi(EmpleadoService service) {
        super(service);
    }
}
