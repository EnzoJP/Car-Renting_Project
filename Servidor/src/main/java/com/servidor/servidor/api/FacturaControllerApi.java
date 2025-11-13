package com.servidor.servidor.api;

import com.servidor.servidor.entities.Factura;
import com.servidor.servidor.services.FacturaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/facturas")
public class FacturaControllerApi extends BaseControllerApi<Factura, Long> {

    public FacturaControllerApi(FacturaService service) {
        super(service);
    }
}
