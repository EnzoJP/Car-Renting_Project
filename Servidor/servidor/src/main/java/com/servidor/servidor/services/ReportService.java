package com.servidor.servidor.services;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

@Service
public class ReportService {
    public byte[] generarReport(String reportName, Map<String, Object> params, Connection connection) throws JRException {
        InputStream reportStream = this.getClass().getResourceAsStream("/reports/" + reportName + ".jasper");
        if (reportStream == null) {
            throw new JRException("No se encontró el reporte: /reports/" + reportName + ".jasper");
        }

        if (params == null) {
            params = java.util.Collections.emptyMap();
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, connection);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
