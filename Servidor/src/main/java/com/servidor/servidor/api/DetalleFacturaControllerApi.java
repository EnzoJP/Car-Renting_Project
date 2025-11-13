package com.servidor.servidor.api;

import com.servidor.servidor.entities.DetalleFactura;
import com.servidor.servidor.services.DetalleFacturaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/detallefacturas")
public class DetalleFacturaControllerApi extends BaseControllerApi<DetalleFactura, Long> {

    public DetalleFacturaControllerApi(DetalleFacturaService service) {
        super(service);
    }
}
