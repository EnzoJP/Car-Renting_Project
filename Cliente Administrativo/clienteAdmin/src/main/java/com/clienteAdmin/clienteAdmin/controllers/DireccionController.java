package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.DTO.DireccionDTO;
import com.clienteAdmin.clienteAdmin.DTO.PaisDTO;
import com.clienteAdmin.clienteAdmin.DTO.ProvinciaDTO;
import com.clienteAdmin.clienteAdmin.DTO.DepartamentoDTO;
import com.clienteAdmin.clienteAdmin.DTO.LocalidadDTO;
import com.clienteAdmin.clienteAdmin.services.DireccionService;
import com.clienteAdmin.clienteAdmin.services.PaisService;
import com.clienteAdmin.clienteAdmin.services.ProvinciaService;
import com.clienteAdmin.clienteAdmin.services.DepartamentoService;
import com.clienteAdmin.clienteAdmin.services.LocalidadService;
import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/direccion")
public class DireccionController extends BaseController<DireccionDTO, Long> {

    @Autowired
    private PaisService paisService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private LocalidadService localidadService;

    public DireccionController(DireccionService service) {
        super(service);
        initController(new DireccionDTO(), "LIST DIRECCION", "EDIT DIRECCION");
    }

    @Override
    protected void preAlta() throws ErrorServiceException {
        this.model.addAttribute("paises", paisService.listarActivos());
        this.model.addAttribute("provincias", provinciaService.listarActivos());
        this.model.addAttribute("departamentos", departamentoService.listarActivos());
        this.model.addAttribute("localidades", localidadService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServiceException {
        preAlta();
    }

    @GetMapping("/filterComboBox")
    @ResponseBody
    public List<?> filterComboBox(@RequestParam String entidad, @RequestParam String nombreAtributoSuperClass, @RequestParam Long idFiltro) {
        switch (entidad) {
            case "Provincia":
                return provinciaService.listarActivos().stream()
                    .filter(p -> p.getPais() != null && p.getPais().getId().equals(idFiltro))
                    .collect(Collectors.toList());
            case "Departamento":
                return departamentoService.listarActivos().stream()
                    .filter(d -> d.getProvincia() != null && d.getProvincia().getId().equals(idFiltro))
                    .collect(Collectors.toList());
            case "Localidad":
                return localidadService.listarActivos().stream()
                    .filter(l -> l.getDepartamento() != null && l.getDepartamento().getId().equals(idFiltro))
                    .collect(Collectors.toList());
            default:
                return List.of();
        }
    }
}
