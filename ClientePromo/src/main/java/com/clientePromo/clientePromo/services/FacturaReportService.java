package com.clientePromo.clientePromo.services;

import com.clientePromo.clientePromo.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacturaReportService {
    @Autowired
    private AuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "https://api.sprintproject.tech/api/v1/facturas/reportes";

    //Descarga la factura PDF de un alquiler específico
    public byte[] descargarFacturaPorAlquiler(Long alquilerId) {
        String url = baseUrl + "/" + alquilerId + "/pdf";
        HttpHeaders headers = authService.authHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_PDF));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al descargar factura: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    //Descarga la factura PDF de múltiples alquileres
    public byte[] descargarFacturaPorAlquileres(List<Long> alquilerIds, String codigoPromocion) {
        String url = baseUrl + "/multiples/pdf";

        HttpHeaders headers = authService.authHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_PDF));

        Map<String, Object> request = new HashMap<>();
        request.put("alquilerIds", alquilerIds);
        request.put("codigoPromocion", codigoPromocion);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al descargar factura múltiple: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
