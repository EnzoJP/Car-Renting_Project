package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.DTO.ClienteDTO;
import com.clientePromo.clientePromo.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClienteService extends BaseServiceClient<ClienteDTO, Long> {

    private final AuthService authService;
    private final RestTemplate restTemplate;

    @Autowired
    public ClienteService(AuthService authService, RestTemplate restTemplate) {
        super("http://localhost:9000/api/v1/clientes", ClienteDTO.class);
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    // endpoint debe ser algo asi: /clientes/usuario/{id}
    public ClienteDTO findByUsuarioId(Long usuarioId) {
        HttpEntity<Void> entity = new HttpEntity<>(authService.authHeaders());

        ResponseEntity<ClienteDTO> response = restTemplate.exchange(
                apiUrl + "/usuario/" + usuarioId,
                HttpMethod.GET,
                entity,
                dtoClass // ClienteDTO
        );
        return response.getBody();
    }
}