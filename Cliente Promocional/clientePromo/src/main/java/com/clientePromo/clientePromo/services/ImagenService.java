package com.clientePromo.clientePromo.services;


import com.clientePromo.clientePromo.DTO.ImagenDTO;
import org.springframework.stereotype.Service;

@Service
public class ImagenService extends BaseServiceClient<ImagenDTO, Long> {
    public ImagenService() {
        super("https://api.sprintproject.tech/api/v1/imagenes", ImagenDTO.class);
    }
}
