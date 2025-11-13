package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.ContactoTelefonicoDTO;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import com.clienteAdmin.clienteAdmin.services.ContactoTelefonicoService;
import com.clienteAdmin.clienteAdmin.services.PersonaService; // Mantenemos este
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contactotelefonico")
public class ContactoTelefonicoController extends BaseController<ContactoTelefonicoDTO, Long> {

    private final ContactoTelefonicoService contactoTelefonicoService;

    @Autowired
    private PersonaService personaService; // Usamos PersonaService para obtener ambos

    public ContactoTelefonicoController(ContactoTelefonicoService contactoTelefonicoService) {
        super(contactoTelefonicoService);
        this.contactoTelefonicoService = contactoTelefonicoService;

        initController(
                new ContactoTelefonicoDTO(),
                "Listado de Contactos Telefónicos",
                "Gestión de Contactos Telefónicos",
                "view/admin/Menu/contacto/"
        );
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        // MODIFICADO: Enviamos listas separadas y ordenadas
        model.addAttribute("clientes", personaService.listarClientesActivosOrdenados());
        model.addAttribute("empleados", personaService.listarEmpleadosActivosOrdenados());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        // MODIFICADO: Enviamos listas separadas y ordenadas
        model.addAttribute("clientes", personaService.listarClientesActivosOrdenados());
        model.addAttribute("empleados", personaService.listarEmpleadosActivosOrdenados());
    }

    @Override
    protected void preActualziacion() throws ErrorServiceException {
        System.out.println("Validando contacto telefónico antes de guardar...");
    }

    @Override
    protected void postActualziacion() throws ErrorServiceException {
        System.out.println("Contacto telefónico guardado correctamente.");
    }
}