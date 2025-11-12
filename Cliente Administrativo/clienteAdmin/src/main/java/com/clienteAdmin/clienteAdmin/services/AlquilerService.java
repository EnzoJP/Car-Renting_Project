package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.AlquilerDTO;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.text.SimpleDateFormat;

@Service
public class AlquilerService extends BaseServiceClient<AlquilerDTO, Long> {

    private final ObjectMapper objectMapper;

    public AlquilerService() {
        super("http://localhost:9000/api/v1/alquileres", AlquilerDTO.class);

        // Configurar ObjectMapper para manejar Fechas (Date)
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Formatear fechas como "yyyy-MM-dd"
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    // --- ¡NUEVO MÉTODO PARA SUBIR ARCHIVOS! ---
    public AlquilerDTO saveWithFile(AlquilerDTO alquiler, byte[] fileContent, String originalFileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(authService.getToken()); // Autenticación

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // 1. Convertir el AlquilerDTO a String JSON
            String alquilerJson = objectMapper.writeValueAsString(alquiler);
            body.add("alquiler", alquilerJson);

            // 2. Añadir el archivo (si existe)
            if (fileContent != null && fileContent.length > 0) {
                ByteArrayResource fileResource = new ByteArrayResource(fileContent) {
                    @Override
                    public String getFilename() {
                        return originalFileName; // Importante para el servidor
                    }
                };
                body.add("file", fileResource);
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. Llamar al nuevo endpoint en el servidor
            ResponseEntity<AlquilerDTO> response = restTemplate.postForEntity(
                    apiUrl + "/save-with-file", // El endpoint multipart
                    requestEntity,
                    AlquilerDTO.class
            );

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el alquiler con archivo: " + e.getMessage());
        }
    }
}