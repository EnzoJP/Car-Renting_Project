package com.servidor.servidor.services;


import com.servidor.servidor.entities.Alquiler;
import com.servidor.servidor.entities.DetalleFactura;
import com.servidor.servidor.entities.Factura;
import com.servidor.servidor.entities.FormaDePago;
import com.servidor.servidor.enums.EstadoFactura;
import com.servidor.servidor.enums.TipoPago;
import com.servidor.servidor.repositories.FacturaRepository;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class FacturaReportService {
    @Autowired
    private ReportService reportService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private FacturaService facturaService;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private AlquilerService alquilerService;

    //Genera una factura PDF para un alquiler específico
    @Transactional
    public byte[] generarFacturaPorAlquiler(Long alquilerId) throws JRException, SQLException {
        //obtener el alquiler
        Alquiler alquiler = alquilerService.getOne(alquilerId);
        if (alquiler == null) {
            throw new IllegalArgumentException("Alquiler no encontrado: " + alquilerId);
        }
        //buscar si ya existe una factura para este alquiler
        Factura factura = buscarFacturaPorAlquiler(alquilerId);

        if (factura == null) {
            factura = crearFacturaParaAlquiler(alquiler);
        }
        //Generar el PDF
        return generarPDFFactura(factura.getId());
    }


    //Genera una factura PDF para múltiples alquileres
    @Transactional
    public byte[] generarFacturaPorAlquileres(List<Long> alquilerIds, String codigoPromocion)
            throws JRException, SQLException {
        if (alquilerIds == null || alquilerIds.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un alquiler");
        }
        //Obtener los alquileres
        List<Alquiler> alquileres = new ArrayList<>();
        for (Long id : alquilerIds) {
            Alquiler alq = alquilerService.getOne(id);
            if (alq != null) {
                alquileres.add(alq);
            }
        }
        if (alquileres.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron alquileres válidos");
        }
        Factura factura = crearFacturaParaAlquileres(alquileres, codigoPromocion);
        // Generar el PDF
        return generarPDFFactura(factura.getId());
    }

    //Genera el PDF de una factura existente
    public byte[] generarPDFFactura(Long facturaId) throws JRException, SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("facturaId", facturaId.toString());

        try (Connection conn = dataSource.getConnection()) {
            return reportService.generarReport("factura", params, conn);
        }
    }

    //Busca si ya existe una factura para un alquiler
    private Factura buscarFacturaPorAlquiler(Long alquilerId) {
        List<Factura> facturas = facturaRepository.findAll();
        for (Factura f : facturas) {
            if (f.getDetalles() != null) {
                for (DetalleFactura detalle : f.getDetalles()) {
                    if (detalle.getAlquileres() != null) {
                        for (Alquiler alq : detalle.getAlquileres()) {
                            if (alq.getId().equals(alquilerId)) {
                                return f;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //Crea una nueva factura para un alquiler
    private Factura crearFacturaParaAlquiler(Alquiler alquiler) {
        Factura factura = new Factura();
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setFechaFactura(new Date());
        factura.setTotalPagado(alquiler.getCosto());
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setEliminado(false);
        // Forma de pago
        FormaDePago formaPago = new FormaDePago();
        formaPago.setTipoPago(TipoPago.BILLETERA_VIRTUAL);
        formaPago.setObservacion("Mercado Pago");
        formaPago.setEliminado(false);
        factura.setFormaDePago(formaPago);
        // Detalle de factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setCantidad(1);
        detalle.setSubtotal(alquiler.getCosto());
        detalle.setAlquileres(Arrays.asList(alquiler));
        detalle.setEliminado(false);
        factura.setDetalles(Arrays.asList(detalle));

        return facturaRepository.save(factura);
    }

    //Crea una factura para muchos alquileres
    private Factura crearFacturaParaAlquileres(List<Alquiler> alquileres, String codigoPromocion) {
        Factura factura = new Factura();
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setFechaFactura(new Date());
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setEliminado(false);
        // Calcular total
        double total = alquileres.stream()
                .mapToDouble(Alquiler::getCosto)
                .sum();
        factura.setTotalPagado(total);
        // Forma de pago
        FormaDePago formaPago = new FormaDePago();
        formaPago.setTipoPago(TipoPago.BILLETERA_VIRTUAL);
        formaPago.setObservacion("Mercado Pago");
        formaPago.setEliminado(false);
        factura.setFormaDePago(formaPago);
        // Detalle de factura
        DetalleFactura detalle = new DetalleFactura();
        detalle.setCantidad(alquileres.size());
        detalle.setSubtotal(total);
        detalle.setAlquileres(alquileres);
        detalle.setEliminado(false);
        factura.setDetalles(Arrays.asList(detalle));

        return facturaRepository.save(factura);
    }


    //Genera un numero de factura secuencial
    private long generarNumeroFactura() {
        List<Factura> facturas = facturaRepository.findAll();
        return facturas.stream()
                .mapToLong(Factura::getNumeroFactura)
                .max()
                .orElse(1000) + 1;
    }
}

