package com.clienteAdmin.clienteAdmin.services;

import com.clienteAdmin.clienteAdmin.DTO.AlquilerDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EXCEL {

    @Autowired
    private AlquilerService alquilerService;

    /**
     * Genera un Excel con dos hojas:
     * - "Alquileres": filas con cliente, vehiculo (marca modelo patente), fecha desde/hasta, costo
     * - "Resumen por Modelo": filas con modelo, cantidad de alquileres, total recaudado
     *
     * Parametros 'desde' y 'hasta' opcionales en formato yyyy-MM-dd; si son null se consideran todos los registros.
     */
    public void generateExcel(OutputStream out, String desde, String hasta) throws Exception {
        List<AlquilerDTO> alquileres = alquilerService.listarActivos();

        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        try {
            if (desde != null && !desde.isBlank()) fechaDesde = LocalDate.parse(desde);
            if (hasta != null && !hasta.isBlank()) fechaHasta = LocalDate.parse(hasta);
        } catch (Exception e) {

            fechaDesde = null; fechaHasta = null;
        }

        
        List<AlquilerDTO> filtrados = new ArrayList<>();
        for (AlquilerDTO a : alquileres) {
            if (a.getFechaDesde() == null) continue;
            LocalDate f = a.getFechaDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (fechaDesde != null && f.isBefore(fechaDesde)) continue;
            if (fechaHasta != null && f.isAfter(fechaHasta)) continue;
            filtrados.add(a);
        }

        // Map para resumen por modelo (clave = marca + " | " + modelo)
        Map<String, Summary> resumen = new HashMap<>();

        for (AlquilerDTO a : filtrados) {
            String marca = "";
            String modelo = "";
            String patente = "";
            if (a.getVehiculo() != null) {
                if (a.getVehiculo().getPatente() != null) patente = a.getVehiculo().getPatente();
                if (a.getVehiculo().getCaracteristicaVehiculo() != null) {
                    marca = a.getVehiculo().getCaracteristicaVehiculo().getMarca();
                    modelo = a.getVehiculo().getCaracteristicaVehiculo().getModelo();
                }
            }
            String key = (marca == null ? "" : marca) + " | " + (modelo == null ? "" : modelo);
            Summary s = resumen.getOrDefault(key, new Summary(marca, modelo));
            s.count++;
            s.total += a.getCosto();
            resumen.put(key, s);
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Hoja 1: Alquileres
            XSSFSheet sheet1 = workbook.createSheet("Alquileres");
            int rownum = 0;
            Row header = sheet1.createRow(rownum++);
            header.createCell(0).setCellValue("Cliente");
            header.createCell(1).setCellValue("Marca");
            header.createCell(2).setCellValue("Modelo");
            header.createCell(3).setCellValue("Patente");
            header.createCell(4).setCellValue("Fecha Desde");
            header.createCell(5).setCellValue("Fecha Hasta");
            header.createCell(6).setCellValue("Costo");

            DataFormat df = workbook.createDataFormat();
            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(df.getFormat("$#,##0.00"));

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

            for (AlquilerDTO a : filtrados) {
                Row r = sheet1.createRow(rownum++);
                String cliente = "";
                try { cliente = a.getCliente().getApellido() + ", " + a.getCliente().getNombre(); } catch (Exception ex) { /* ignore */ }
                r.createCell(0).setCellValue(cliente);
                String marca = "";
                String modelo = "";
                String patente = "";
                if (a.getVehiculo() != null) {
                    patente = a.getVehiculo().getPatente() == null ? "" : a.getVehiculo().getPatente();
                    if (a.getVehiculo().getCaracteristicaVehiculo() != null) {
                        marca = a.getVehiculo().getCaracteristicaVehiculo().getMarca() == null ? "" : a.getVehiculo().getCaracteristicaVehiculo().getMarca();
                        modelo = a.getVehiculo().getCaracteristicaVehiculo().getModelo() == null ? "" : a.getVehiculo().getCaracteristicaVehiculo().getModelo();
                    }
                }
                r.createCell(1).setCellValue(marca);
                r.createCell(2).setCellValue(modelo);
                r.createCell(3).setCellValue(patente);
                Cell c4 = r.createCell(4);
                if (a.getFechaDesde() != null) c4.setCellValue(sdf.format(a.getFechaDesde())); else c4.setCellValue("");
                Cell c5 = r.createCell(5);
                if (a.getFechaHasta() != null) c5.setCellValue(sdf.format(a.getFechaHasta())); else c5.setCellValue("");
                Cell c6 = r.createCell(6);
                c6.setCellValue(a.getCosto());
                c6.setCellStyle(moneyStyle);
            }

            for (int i = 0; i <= 6; i++) sheet1.autoSizeColumn(i);

            // Hoja 2: Resumen por modelo
            XSSFSheet sheet2 = workbook.createSheet("Resumen por Modelo");
            int r2 = 0;
            Row h2 = sheet2.createRow(r2++);
            h2.createCell(0).setCellValue("Marca");
            h2.createCell(1).setCellValue("Modelo");
            h2.createCell(2).setCellValue("Cantidad Alquileres");
            h2.createCell(3).setCellValue("Total Recaudado");

            for (Map.Entry<String, Summary> e : resumen.entrySet()) {
                Summary s = e.getValue();
                Row row = sheet2.createRow(r2++);
                row.createCell(0).setCellValue(s.marca == null ? "" : s.marca);
                row.createCell(1).setCellValue(s.modelo == null ? "" : s.modelo);
                row.createCell(2).setCellValue(s.count);
                Cell totalCell = row.createCell(3);
                totalCell.setCellValue(s.total);
                totalCell.setCellStyle(moneyStyle);
            }

            for (int i = 0; i <= 3; i++) sheet2.autoSizeColumn(i);

            workbook.write(out);
            out.flush();
        }
    }

    private static class Summary {
        String marca;
        String modelo;
        int count = 0;
        double total = 0.0;

        Summary(String marca, String modelo) {
            this.marca = marca;
            this.modelo = modelo;
        }
    }
}
