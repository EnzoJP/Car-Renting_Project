package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.AlquilerDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class PDF {

    @Autowired
    private AlquilerService alquilerService;


    public void generateDocument(OutputStream out) throws Exception {
        List<AlquilerDTO> alquileres = alquilerService.listarActivos();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);

        writer.setCloseStream(false);
        document.open();

        // Título
        document.add(new Paragraph("Reporte de Alquileres"));

        //espacio
        document.add(new Paragraph(" "));

        // Tabla
        PdfPTable table = new PdfPTable(5);
        table.addCell("Cliente");
        table.addCell("Auto");
        table.addCell("Fecha de Alquiler");
        table.addCell("Fecha Hasta");
        table.addCell("Monto Pagado");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat money = new DecimalFormat("#,##0.00");

        for (AlquilerDTO alquiler : alquileres) {
            String cliente = alquiler.getCliente() != null ? alquiler.getCliente().getNombre() + " " + alquiler.getCliente().getApellido() : "N/A";
            String auto = alquiler.getVehiculo() != null && alquiler.getVehiculo().getCaracteristicaVehiculo() != null ?
                    alquiler.getVehiculo().getCaracteristicaVehiculo().getMarca() + " " + alquiler.getVehiculo().getCaracteristicaVehiculo().getModelo() : "N/A";
            String fecha = alquiler.getFechaDesde() != null ? sdf.format(alquiler.getFechaDesde()) : "N/A";
            String fechaHasta = alquiler.getFechaHasta() != null ? sdf.format(alquiler.getFechaHasta()) : "N/A";
            String monto = "N/A";
            try { monto = "$" + money.format(alquiler.getCosto()); } catch (Exception ex) { /* ignore */ }

            table.addCell(cliente);
            table.addCell(auto);
            table.addCell(fecha);
            table.addCell(fechaHasta);
            table.addCell(monto);
        }

        document.add(table);
        document.close();


    }
}
