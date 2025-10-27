package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.ImagenDTO;
import org.springframework.stereotype.Service;

@Service
public class ImagenService extends BaseServiceClient<ImagenDTO, Long> {
    public ImagenService() {
        super("http://localhost:9000/api/v1/imagenes", ImagenDTO.class);
    }
}
