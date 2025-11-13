package com.servidor.servidor.api;

import com.servidor.servidor.services.FacturaReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas/reportes")
public class FacturaReportControllerApi {
    @Autowired
    private FacturaReportService facturaReportService;

    //Genera factura para un alquiler
    @GetMapping("/{alquilerId}/pdf")
    public ResponseEntity<byte[]> generarFacturaPorAlquiler(@PathVariable Long alquilerId) {
        try {
            byte[] pdfBytes = facturaReportService.generarFacturaPorAlquiler(alquilerId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "factura_" + alquilerId + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //Genera factura para muchos alquileres
    @PostMapping("/multiples/pdf")
    public ResponseEntity<byte[]> generarFacturaPorAlquileres(
            @RequestBody FacturaMultipleRequest request) {
        try {
            byte[] pdfBytes = facturaReportService.generarFacturaPorAlquileres(
                    request.getAlquilerIds(),
                    request.getCodigoPromocion()
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "factura_multiple.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // DTO para recibir muchos IDs
    public static class FacturaMultipleRequest {
        private List<Long> alquilerIds;
        private String codigoPromocion;
        public List<Long> getAlquilerIds() { return alquilerIds; }
        public void setAlquilerIds(List<Long> alquilerIds) { this.alquilerIds = alquilerIds; }
        public String getCodigoPromocion() { return codigoPromocion; }
        public void setCodigoPromocion(String codigoPromocion) { this.codigoPromocion = codigoPromocion; }
    }
}

