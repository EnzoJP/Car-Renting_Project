package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.DTO.AlquilerDTO;
import com.clientePromo.clientePromo.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class AlquilerService extends BaseServiceClient<AlquilerDTO, Long> {

    @Autowired
    private AuthService authService; //obtener token guardado

    public AlquilerService() {
        super("http://localhost:9000/api/v1/alquileres", AlquilerDTO.class);
    }

    public List<AlquilerDTO> findAlquileresPorCliente(Long clienteId) {
        String url = "http://localhost:9000/api/v1/alquileres?cliente.id=" + clienteId;

        // obtener el token desde AuthService
        String token = authService.getToken();

        //crear headers con el Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AlquilerDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                AlquilerDTO[].class
        );


        return Arrays.asList(response.getBody());
    }

}

