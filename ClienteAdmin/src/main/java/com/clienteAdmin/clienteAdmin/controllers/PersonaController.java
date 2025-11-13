package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.PersonaDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.PersonaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personas")
public class PersonaController extends BaseController<PersonaDTO, Long> {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        super(personaService);
        this.personaService = personaService;

        initController(
                new PersonaDTO(),
                "Listado de Personas",
                "Gestión de Personas",
                "view/admin/Menu/persona/"
        );

        this.nameEntityLower = "personas"; // importante para el th:href del template
    }

    @Override
    protected void preActualziacion() throws ErrorServiceException {
        System.out.println("Validando persona antes de guardar...");
    }

    @Override
    protected void postActualziacion() throws ErrorServiceException {
        System.out.println("Persona guardada correctamente.");
    }
}

