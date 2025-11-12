package  com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.EmpresaDTO;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService extends BaseServiceClient<EmpresaDTO, Long> {
    public EmpresaService() {
        super("https://api.sprintproject.tech/api/v1/empresas", EmpresaDTO.class);
    }
}