package com.clienteAdmin.clienteAdmin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracionCorreoAutomaticoDTO implements Identifiable<Long> {
    private Long id;
    private String correo;
    private String clave;
    private String puerto;
    private String smtp;
    private boolean tls;
    private EmpresaDTO empresa;
}
