package  com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.DocumentacionDTO;
import org.springframework.stereotype.Service;

@Service
public class DocumentacionService extends BaseServiceClient<DocumentacionDTO, Long> {
    public DocumentacionService() {
        super("https://api.sprintproject.tech/api/v1/documentaciones", DocumentacionDTO.class);
    }
}