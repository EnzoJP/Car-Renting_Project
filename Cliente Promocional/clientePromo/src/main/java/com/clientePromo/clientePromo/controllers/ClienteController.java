package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.DTO.AlquilerDTO;
import com.clientePromo.clientePromo.DTO.ClienteDTO;
import com.clientePromo.clientePromo.DTO.UsuarioDTO;
import com.clientePromo.clientePromo.auth.AuthService;
import com.clientePromo.clientePromo.models.WeatherResponse;
import com.clientePromo.clientePromo.services.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final WeatherService weatherService;
    private final DolarService dolarService;
    private final ClienteService clienteService;
    private final AlquilerService alquilerService;
    private final AuthService authService;
    @Autowired
    private final FacturaReportService facturaReportService;

    @Autowired
    public ClienteController(WeatherService weatherService, DolarService dolarService, ClienteService clienteService, AlquilerService alquilerService, AuthService authService, FacturaReportService facturaReportService) {
        this.weatherService = weatherService;
        this.dolarService = dolarService;
        this.clienteService = clienteService;
        this.alquilerService = alquilerService;
        this.authService = authService;
        this.facturaReportService = facturaReportService;
    }

    @GetMapping("/dashboard")
    public String clienteDashboard(Model model) {

        try {
            //obtengo cliente autenticAado
            UsuarioDTO usuarioLogueado = authService.getUsuarioAutenticado();

            //busvo x usuario
            ClienteDTO cliente = clienteService.findByUsuarioId(usuarioLogueado.getId());
            model.addAttribute("cliente", cliente);
            model.addAttribute("usuario", usuarioLogueado);
            System.out.println(cliente.getId());
            System.out.println(usuarioLogueado.getId());
            //historial de alquileres
            List<AlquilerDTO> alquileres = alquilerService.findAlquileresPorCliente(cliente.getId());

            // calcular total solo de alquileres ADEUDADO
            double totalAdeudado = alquileres.stream()
                    .filter(a -> a.getEstadoAlquiler() == null || a.getEstadoAlquiler().name().equals("ADEUDADO") || a.getEstadoAlquiler() == com.clientePromo.clientePromo.DTO.EstadoAlquiler.ADEUDADO)
                    .mapToDouble(a -> a.getCosto() != null ? a.getCosto() : 0.0)
                    .sum();

            model.addAttribute("alquileres", alquileres);
            model.addAttribute("total", totalAdeudado);

            cargarDatosNavbar(model);

            model.addAttribute("titulo", "Mi Dashboard - Sprint");
            return "view/cliente/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No se pudo cargar tu sesión. Por favor, inicia sesión de nuevo.");
            return "redirect:/login?error=session_expired";
        }
    }

    @GetMapping("/mi-imagen")
    @ResponseBody
    public ResponseEntity<byte[]> getImagenPerfil() {

        try {
            //obtengo cliente
            UsuarioDTO usuarioLogueado = authService.getUsuarioAutenticado();
            ClienteDTO cliente = clienteService.findByUsuarioId(usuarioLogueado.getId());

            if (cliente == null ||
                    cliente.getImagen() == null ||
                    cliente.getImagen().getContenido() == null ||
                    cliente.getImagen().getMime() == null) {

                return ResponseEntity.notFound().build();
            }
            byte[] imagenBytes = cliente.getImagen().getContenido();
            String mimeType = cliente.getImagen().getMime();

            //decuelvo imagen con su tipo de contenido
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(imagenBytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void cargarDatosNavbar(Model model) {
        try {
            List<String> dolarData = dolarService.obtenerCompraVentaFecha();
            if (dolarData != null && dolarData.size() >= 2) {
                model.addAttribute("venta", dolarData.get(1));
            }
        } catch (Exception e) {
            model.addAttribute("venta", "N/A");
        }

        try {
            WeatherResponse weather = weatherService.getWeatherByCity("Godoy Cruz").block();
            model.addAttribute("weather", weather);
        } catch (Exception e) {
            model.addAttribute("weather", null);
        }
    }

    @GetMapping("/factura/{alquilerId}")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Long alquilerId) {
        try {
            // obtener datos del alquiler desde el servicio existente
            AlquilerDTO alquiler = alquilerService.obtener(alquilerId);
            if (alquiler == null) {
                return ResponseEntity.notFound().build();
            }

            ClienteDTO cliente = alquiler.getCliente();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();


            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);


            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1f, 3f});

            
            try {
                ClassPathResource imgFile = new ClassPathResource("static/images/logo_2.png");
                InputStream is = imgFile.getInputStream();
                Image logo = Image.getInstance(is.readAllBytes());
                logo.scaleToFit(80, 80);
                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(logoCell);
            } catch (Exception ex) {
                PdfPCell empty = new PdfPCell(new Phrase(""));
                empty.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(empty);
            }

            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(Rectangle.NO_BORDER);
            infoCell.addElement(new Paragraph("Sprint Company", titleFont));
            infoCell.addElement(new Paragraph("Avenida Siempreviva 742", normal));
            infoCell.addElement(new Paragraph("Mendoza", normal));
            infoCell.addElement(new Paragraph("emaildepruebasprint@gmail.com", normal));
            headerTable.addCell(infoCell);

            document.add(headerTable);

            // linea separadora
            LineSeparator ls = new LineSeparator();
            ls.setLineColor(BaseColor.BLACK);
            document.add(new Chunk(ls));

            // Datos del receptor
            document.add(new Paragraph("Facturar a:", normalBold));
            String nombreCliente = cliente != null ? (cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : "")) : "Cliente";
            document.add(new Paragraph(nombreCliente, normal));
            document.add(Chunk.NEWLINE);

            // Tabla de items (aquí solo 1 item: 'Alquiler')
            PdfPTable items = new PdfPTable(4);
            items.setWidthPercentage(100);
            items.setWidths(new float[]{6f, 1f, 2f, 2f});

            // encabezados
            PdfPCell c1 = new PdfPCell(new Phrase("Descripción", normalBold));
            c1.setBorder(Rectangle.NO_BORDER);
            items.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Cantidad", normalBold));
            c2.setBorder(Rectangle.NO_BORDER);
            items.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Phrase("Precio Unit.", normalBold));
            c3.setBorder(Rectangle.NO_BORDER);
            items.addCell(c3);
            PdfPCell c4 = new PdfPCell(new Phrase("Subtotal", normalBold));
            c4.setBorder(Rectangle.NO_BORDER);
            items.addCell(c4);

            // fila item
            String descripcion = "Alquiler ID " + alquiler.getId();
            if (alquiler.getVehiculo() != null && alquiler.getVehiculo().getCaracteristicaVehiculo() != null) {
                descripcion += " - " + alquiler.getVehiculo().getCaracteristicaVehiculo().getMarca() + " " + alquiler.getVehiculo().getCaracteristicaVehiculo().getModelo();
            }
            PdfPCell d1 = new PdfPCell(new Phrase(descripcion, normal)); d1.setBorder(Rectangle.NO_BORDER); items.addCell(d1);
            PdfPCell d2 = new PdfPCell(new Phrase("1", normal)); d2.setBorder(Rectangle.NO_BORDER); items.addCell(d2);
            String precioUnit = alquiler.getCosto() != null ? String.format("$%.2f", alquiler.getCosto()) : "$0.00";
            PdfPCell d3 = new PdfPCell(new Phrase(precioUnit, normal)); d3.setBorder(Rectangle.NO_BORDER); items.addCell(d3);
            PdfPCell d4 = new PdfPCell(new Phrase(precioUnit, normal)); d4.setBorder(Rectangle.NO_BORDER); items.addCell(d4);

            document.add(items);

            // espacio
            document.add(Chunk.NEWLINE);

            // Detalle factura
            document.add(new Paragraph("Detalle Factura:", normalBold));
            document.add(Chunk.NEWLINE);

            PdfPTable detalle = new PdfPTable(2);
            detalle.setWidthPercentage(60);
            detalle.setHorizontalAlignment(Element.ALIGN_LEFT);
            detalle.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            detalle.addCell(new Phrase("Número:", normalBold));
            detalle.addCell(new Phrase(alquiler.getId().toString(), normal));

            detalle.addCell(new Phrase("Fecha:", normalBold));
            String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            detalle.addCell(new Phrase(fechaStr, normal));

            detalle.addCell(new Phrase("Estado:", normalBold));
            String estado = alquiler.getEstadoAlquiler() != null ? alquiler.getEstadoAlquiler().name() : "PAGADA";
            detalle.addCell(new Phrase(estado, normal));

            detalle.addCell(new Phrase("Forma de Pago:", normalBold));
            detalle.addCell(new Phrase("Pago via sitio", normal));

            document.add(detalle);

            // Total a la derecha
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{6f, 2f});
            totalTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(new Phrase("", normal));
            PdfPCell totalLabel = new PdfPCell(new Phrase("Total Pagado:", normalBold));
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(totalLabel);

            totalTable.addCell(new Phrase("", normal));
            PdfPCell totalValue = new PdfPCell(new Phrase(precioUnit, titleFont));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValue.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(totalValue);

            document.add(totalTable);

            // Firma (texto estilizado)
            document.add(Chunk.NEWLINE);
            Paragraph firma = new Paragraph("Sprint", new Font(Font.FontFamily.HELVETICA, 20, Font.ITALIC));
            firma.setAlignment(Element.ALIGN_RIGHT);
            document.add(firma);

            document.close();

            byte[] pdfBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "factura_" + alquilerId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
