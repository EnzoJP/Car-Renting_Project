package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.models.dolar;
import com.clientePromo.clientePromo.services.DolarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dolar")
public class DolarController {

    @Autowired
    private DolarService dolarService;

    @GetMapping
    public List<dolar> getDolars() {
        return dolarService.obtenerDatosDolar();
    }

    @GetMapping("/compra-venta-fecha")
    public List<String> getCompraVentaFecha() {
        return dolarService.obtenerCompraVentaFecha();
    }

}