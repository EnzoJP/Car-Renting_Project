package com.servidor.servidor.api;

import com.servidor.servidor.entities.Usuario;
import com.servidor.servidor.services.UsuarioService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/usuarios")
public class UsuarioControllerApi extends BaseControllerApi<Usuario, Long> {

    public UsuarioControllerApi(UsuarioService service) {
        super(service);
    }
}
