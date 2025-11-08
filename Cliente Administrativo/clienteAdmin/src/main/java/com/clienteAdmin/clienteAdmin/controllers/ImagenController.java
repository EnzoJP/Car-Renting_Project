package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ImagenDTO;
import com.clienteAdmin.clienteAdmin.services.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/imagen")
public class ImagenController extends BaseController<ImagenDTO, Long> {

    @Autowired
    private ImagenService imagenService;

    public ImagenController(ImagenService service) {
        super(service);
        initController(new ImagenDTO(),
                "LIST IMAGEN",
                "EDIT IMAGEN",
                "view/admin/Menu/utils/");
    }
}
