package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ContactoCorreoElectronicoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.ContactoCorreoElectronicoService;
import com.clienteAdmin.clienteAdmin.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contactocorreo")
public class ContactoCorreoElectronicoController extends BaseController<ContactoCorreoElectronicoDTO, Long> {

    private final ContactoCorreoElectronicoService contactoCorreoElectronicoService;

    @Autowired
    private PersonaService personaService;

    public ContactoCorreoElectronicoController(ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
        super(contactoCorreoElectronicoService);
        this.contactoCorreoElectronicoService = contactoCorreoElectronicoService;

        initController(
                new ContactoCorreoElectronicoDTO(),
                "Listado de Correos Electrónicos",
                "Gestión de Correos Electrónicos",
                "view/admin/Menu/contacto/"
        );
        this.nameEntityLower = "contactocorreo";
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        model.addAttribute("personas", personaService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        model.addAttribute("personas", personaService.listarActivos());
    }

    @Override
    protected void preActualziacion() throws ErrorServiceException {
        System.out.println("Validando correo antes de guardar...");
    }

    @Override
    protected void postActualziacion() throws ErrorServiceException {
        System.out.println("Correo guardado correctamente.");
    }
}
