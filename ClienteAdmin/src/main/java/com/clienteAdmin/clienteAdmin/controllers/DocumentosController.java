package com.clienteAdmin.clienteAdmin.controllers;

import com.clienteAdmin.clienteAdmin.services.EXCEL;
import com.clienteAdmin.clienteAdmin.services.PDF;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DocumentosController {

    @Autowired
    private PDF pdfService;

    @Autowired
    private EXCEL excelService;

    @GetMapping("/generar-pdf")
    public void generarPdf(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reportes_alquileres.pdf");

            pdfService.generateDocument(response.getOutputStream());
            response.getOutputStream().flush();

        } catch (Exception e) {
            // imprimir stack para debugging; puede reemplazarse por logger
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generar-excel.xlsx")
    public void generarExcel(HttpServletResponse response,
                             @RequestParam(name = "desde", required = false) String desde,
                             @RequestParam(name = "hasta", required = false) String hasta) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=reportes_alquileres.xlsx");

            excelService.generateExcel(response.getOutputStream(), desde, hasta);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
