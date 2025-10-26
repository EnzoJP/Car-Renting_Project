package com.servidor.servidor.api;

import com.servidor.servidor.entities.Documentacion;
import com.servidor.servidor.services.DocumentacionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/documentaciones")
public class DocumentacionControllerApi extends BaseControllerApi<Documentacion, Long> {

    public DocumentacionControllerApi(DocumentacionService service) {
        super(service);
    }
}
