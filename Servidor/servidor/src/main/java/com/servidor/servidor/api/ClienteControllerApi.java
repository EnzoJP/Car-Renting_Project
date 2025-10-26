package com.servidor.servidor.api;

import com.servidor.servidor.entities.Cliente;
import com.servidor.servidor.services.ClienteService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/clientes")
public class ClienteControllerApi extends BaseControllerApi<Cliente, Long> {

    public ClienteControllerApi(ClienteService service) {
        super(service);
    }
}
