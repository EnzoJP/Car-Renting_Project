package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.PromocionDTO;
import com.clienteAdmin.clienteAdmin.services.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/promociones")
public class PromocionController extends BaseController<PromocionDTO, Long> {

    @Autowired
    public PromocionController(PromocionService service) {
        super(service);

        initController(
                new PromocionDTO(),
                "Gestión de Promociones",
                "Formulario de Promoción",
                "view/admin/Menu/promociones/"
        );
    }

}