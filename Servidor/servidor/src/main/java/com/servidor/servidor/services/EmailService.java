package com.servidor.servidor.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // Recordatorio de devolución de vehículo
    public void enviarRecordatorioDevolucion(String destinatario, String nombreCliente, String vehiculo, LocalDate fechaDevolucion) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Recordatorio: Devolución de vehículo - Sprint Solutions");
            // Formatear fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fechaDevolucion.format(formatter);
            // Crear contenido HTML
            String htmlContent = crearHtmlRecordatorio(nombreCliente, vehiculo, fechaFormateada);
            helper.setText(htmlContent, true);
            // Enviar mail
            mailSender.send(message);
            System.out.println("Email enviado a: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Contenido html del mail
    private String crearHtmlRecordatorio(String nombreCliente, String vehiculo, String fecha) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #fe5b29 0%%, #363636 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .highlight { background: #ffe5dc; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #fe5b29; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    .button { display: inline-block; background: #fe5b29; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚗 Sprint Solutions 😎</h1>
                        <p>Recordatorio de Devolución</p>
                    </div>
                    <div class="content">
                        <h2>¡Hola %s!</h2>
                        <p>Te recordamos que <strong>mañana %s</strong> debes devolver el vehículo que alquilaste. 🤯</p>
                        
                        <div class="highlight">
                            <strong>Detalles de tu reserva:</strong><br>
                            🚙 Vehículo: <strong>%s</strong><br>
                            📅 Fecha de devolución: <strong>%s</strong><br>
                        </div>
                        
                        <p><strong>Importante:</strong></p>
                        <ul>
                            <li>Asegúrate de llenar el tanque</li>
                            <li>Revisa que no dejes objetos personales</li>
                            <li>Verifica el estado del vehículo</li>
                        </ul>
                        
                        <p>Si tienes alguna consulta, no dudes en contactarnos:</p>
                        <p>📞 +54 9 261 756 4235<br>
                        📧 emaildepruebasprint@gmail.com </p>
                        
                        <center>
                            <a href="https://www.sprintproject.tech" class="button">Ver mis reservas</a>
                        </center>
                    </div>
                    <div class="footer">
                        <p>Sprint Solutions - Alquiler de vehículos<br>
                        Mendoza, Argentina</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombreCliente, fecha, vehiculo, fecha);
    }

    //Mail simple
    public void enviarEmailSimple(String destinatario, String asunto, String contenido) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject(asunto);
            message.setText(contenido);

            mailSender.send(message);
            System.out.println("Email simple enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }

    public void enviarEmailMasivoBCC(List<String> destinatarios, String asunto, String contenidoHtml) {
        if (destinatarios == null || destinatarios.isEmpty()) {
            System.out.println("No hay destinatarios para enviar el email masivo.");
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            // Convertir la Lista a un Array para el BCC
            String[] bccArray = destinatarios.toArray(new String[0]);
            helper.setBcc(bccArray);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);
            mailSender.send(message);
            System.out.println("Email de promoción enviado (BCC) a " + destinatarios.size() + " destinatarios.");
        } catch (MessagingException e) {
            System.err.println("Error al enviar email masivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String crearHtmlPromocion(String descripcion, String codigo, double porcentaje, String fechaFin) {
        // Reutilizamos el estilo de tu otro email para mantener la consistencia
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #fe5b29 0%%, #363636 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .highlight { background: #ffe5dc; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #fe5b29; text-align: center; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    .button { display: inline-block; background: #fe5b29; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚗 Sprint Solutions 😎</h1>
                        <p>¡Tenemos una nueva promoción para vos!</p>
                    </div>
                    <div class="content">
                        <h2>¡Hola! No te pierdas esta 🤯🔥oferta especial😼💯:</h2>
                        <p>%s</p>
                        
                        <div class="highlight">
                            <p style="font-size: 20px; margin:0;">Usa el código:</p>
                            <h2 style="font-size: 32px; margin: 5px 0; color: #fe5b29;">%s</h2>
                            <p style="font-size: 18px; margin:0;">y obtén un <strong>%.0f%% de descuento</strong></p>
                        </div>
                        
                        <p>Esta promoción es válida hasta el %s.</p>
                        
                        <center>
                            <a href="https://www.sprintproject.tech" class="button">¡Reserva ahora!🔥🔥🔥</a>
                        </center>
                    </div>
                    <div class="footer">
                        <p>Sprint Solutions - Alquiler de vehículos<br>
                        Mendoza, Argentina</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(descripcion, codigo, porcentaje, fechaFin);
    }

}
