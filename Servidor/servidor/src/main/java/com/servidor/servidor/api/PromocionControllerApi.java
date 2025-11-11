package com.servidor.servidor.api;

import com.servidor.servidor.entities.Promocion;
import com.servidor.servidor.services.PromocionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/promociones")
@CrossOrigin(origins = "*")
public class PromocionControllerApi extends BaseControllerApi<Promocion, Long> {

    public PromocionControllerApi(PromocionService service) {
        super(service);
    }
}
