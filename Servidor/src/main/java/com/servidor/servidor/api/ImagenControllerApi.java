package com.servidor.servidor.api;

import com.servidor.servidor.entities.Imagen;
import com.servidor.servidor.services.ImagenService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/imagenes")
public class ImagenControllerApi extends BaseControllerApi<Imagen, Long> {

    public ImagenControllerApi(ImagenService service) {
        super(service);
    }
}
