package com.clienteAdmin.clienteAdmin.controllers;


import com.clienteAdmin.clienteAdmin.exceptions.ErrorServiceException;

import com.clienteAdmin.clienteAdmin.DTO.Identifiable;
import com.clienteAdmin.clienteAdmin.DTO.ImagenDTO;
import com.clienteAdmin.clienteAdmin.services.BaseServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Controller
public abstract class BaseController<T extends Identifiable<ID>, ID> {

    protected final BaseServiceClient<T, ID> service;
    private String nameClass = "";
    protected String viewList = "";
    protected String redirectList = "";
    protected String viewEdit = "";
    protected T entity;
    protected String nameEntityLower;
    protected Model model;
    protected String titleList;
    protected String titleEdit;
    protected String urlPrefix = "";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected BaseController(BaseServiceClient<T, ID> service) {
        this.service = service;
    }

    /**
     * Inicializa nombres y vistas. Llamar desde el constructor del controlador concreto.
     * ejemplo: initController(new AutorDTO(), "LIST AUTOR", "EDIT AUTOR") por ejemplo;
     */

    protected void initController(T entidad, String titleList, String titleEdit, String viewBasePath) {
        this.entity = entidad;
        this.nameClass = getSimpleName(getNameEntity(entidad));
        this.nameEntityLower = nameClass.toLowerCase();

        // asignamos las vistas usando la ruta que nos hayan pasado desde el controllador hijo
        this.viewList = viewBasePath + "l" + nameClass;
        this.viewEdit = viewBasePath + "e" + nameClass;

        // 2. Lee el @RequestMapping de la clase hija (ej: "/usuario/pais")
        try {
            RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
            if (requestMapping != null && requestMapping.value().length > 0) {
                this.urlPrefix = requestMapping.value()[0];
            }
        } catch (Exception e) {
            this.urlPrefix = ""; // fallback
        }

        //revisar
        this.redirectList = "redirect:" + this.urlPrefix + "/list";

        this.titleList = titleList;
        this.titleEdit = titleEdit;
    }

    @GetMapping("")
    public String index() {
        return redirectList;
    }

    @GetMapping("/list")
    public String listar(Model model) {
        try {
            this.model = model;
            List<T> activos = service.listarActivos();
            this.model.addAttribute("items", activos);
            this.model.addAttribute("titleList", titleList);
            this.model.addAttribute("nameEntityLower", nameEntityLower);
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema");
        }
        return viewList;
    }

    @GetMapping("/alta")
    public String crear(Model model) {
        try {
            this.model = model;
            // crear una nueva instancia vacía para el form (se pasó en initController como 'entity')
            this.model.addAttribute("item", entity);
            this.model.addAttribute("isDisabled", false);
            this.model.addAttribute("titleEdit", titleEdit);
            this.model.addAttribute("nameEntityLower", nameEntityLower);
            preAlta();
            return viewEdit;
        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema");
            return viewEdit;
        }
    }

    @GetMapping("/consultar/{id}")
    public String consultar(@PathVariable ID id, Model model) {
        try {
            this.model = model;
            T found = service.obtener(id);
            if (found == null)
                throw new IllegalArgumentException("No encontrado: " + id);

            this.model.addAttribute("item", found);
            this.model.addAttribute("isDisabled", true);
            this.model.addAttribute("titleEdit", titleEdit);
            this.model.addAttribute("nameEntityLower", nameEntityLower);
            preModificacion();
            return viewEdit;
        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema");
            return viewEdit;
        }
    }

    @GetMapping("/modificar/{id}")
    public String editar(@PathVariable ID id, Model model) {
        try {
            this.model = model;
            T found = service.obtener(id);
            if (found == null)
                throw new IllegalArgumentException("No encontrado: " + id);

            this.model.addAttribute("item", found);
            this.model.addAttribute("isDisabled", false);
            this.model.addAttribute("titleEdit", titleEdit);
            this.model.addAttribute("nameEntityLower", nameEntityLower);
            preModificacion();
            return viewEdit;
        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema");
            return viewEdit;
        }
    }

    @GetMapping("/baja/{id}")
    public String eliminar(@PathVariable ID id, RedirectAttributes attributes, Model model) {
        try {
            this.model = model;
            preBaja();
            service.bajaLogica(id);
            postBaja();
            attributes.addFlashAttribute("msgExito", "La acción fue realizada correctamente.");
            return redirectList;
        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema");
            return viewEdit;
        }
    }

    /**
     * El formulario debe publicar un objeto 'item' con la misma estructura del DTO (incluyendo id).
     */
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute("item") T entidad, @RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes attributes, Model model) {
        try {
            this.model = model;
            preActualziacion();

            if (file != null && !file.isEmpty() && entidad instanceof ImagenDTO) {
                ((ImagenDTO) entidad).setContenido(file.getBytes());
                ((ImagenDTO) entidad).setMime(file.getContentType());
            }

            if (entidad.getId() == null) {
                service.alta(entidad);
            } else {
                service.modificar(entidad.getId(), entidad);
            }

            postActualziacion();
            attributes.addFlashAttribute("msgExito", "La acción fue realizada correctamente.");
            return redirectList;
        } catch (ErrorServiceException e) {
            model.addAttribute("msgError", e.getMessage());
            return viewEdit;
        } catch (Exception e) {
            model.addAttribute("msgError", "Error de Sistema");
            return viewEdit;
        }
    }

    @GetMapping("/cancelar")
    public String cancelar() {
        return redirectList;
    }

    private String getNameEntity(T object) {
        return object.getClass().getSimpleName();
    }

    private String getSimpleName(String simple) {
        if (simple.endsWith("DTO")) {
            simple = simple.substring(0, simple.length() - 3);
        }
        return simple;
    }

    /*  para que las subclases redefinan comportamiento */
    protected void preAlta() throws ErrorServiceException {}
    protected void preModificacion() throws ErrorServiceException {}
    protected void preBaja() throws ErrorServiceException {}
    protected void postBaja() throws ErrorServiceException {}
    protected void preActualziacion() throws ErrorServiceException {}
    protected void postActualziacion() throws ErrorServiceException {}

}
