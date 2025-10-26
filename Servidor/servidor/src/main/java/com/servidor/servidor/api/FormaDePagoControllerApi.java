package com.servidor.servidor.api;

import com.servidor.servidor.entities.FormaDePago;
import com.servidor.servidor.services.FormaDePagoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/formadepagos")
public class FormaDePagoControllerApi extends BaseControllerApi<FormaDePago, Long> {

    public FormaDePagoControllerApi(FormaDePagoService service) {
        super(service);
    }
}
