package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.dto.ConfirmacionReserva;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String DIRECCION = "Primera Ote. Nte. 130, Concepción, 29620 Copainalá, Chis., México";
    private static final String TELEFONO = "968 112 8923";

    @Autowired
    private JavaMailSender mailSender;

    public void enviarConfirmacion(String nombre, String destino, ConfirmacionReserva c) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(destino);
            helper.setSubject("Tu reservación fue realizada exitosamente · Hotel 3 Estrellas");
            helper.setText(construirHtml(nombre, c), true);
            mailSender.send(mensaje);
            log.info("Correo de confirmación enviado a {}", destino);
        } catch (MessagingException | MailException e) {
            // La reservacion ya esta guardada; un fallo de correo no la cancela
            log.error("No se pudo enviar el correo a {}: {}", destino, e.getMessage());
        }
    }

    private String construirHtml(String nombre, ConfirmacionReserva c) {
        boolean pagado = "TARJETA".equals(c.metodoPago());
        String estadoPago = pagado
                ? "<strong style=\"color:#2e7d32\">Pago realizado</strong>"
                : "<strong style=\"color:#b7791f\">Pago pendiente</strong>";
        String total = String.format(Locale.US, "$%,.2f MXN", c.total());

        return """
            <div style="font-family:Arial,sans-serif;max-width:560px;margin:auto;border:1px solid #e5e0d0;border-radius:12px;overflow:hidden">
              <div style="background:#1b2a41;color:#fff;padding:20px;text-align:center">
                <h2 style="margin:0">Hotel 3 Estrellas</h2>
                <p style="margin:6px 0 0">¡Tu reservación fue realizada exitosamente!</p>
              </div>
              <div style="padding:20px">
                <p>Hola <strong>%s</strong>, gracias por reservar con nosotros.</p>
                <table style="width:100%%;border-collapse:collapse">
                  <tr><td style="padding:6px 0">Tipo de habitación</td><td><strong>%s</strong> (habitación %s)</td></tr>
                  <tr><td style="padding:6px 0">Fecha de entrada</td><td>%s</td></tr>
                  <tr><td style="padding:6px 0">Fecha de salida</td><td>%s</td></tr>
                  <tr><td style="padding:6px 0">Noches</td><td>%d</td></tr>
                  <tr><td style="padding:6px 0">Total</td><td><strong>%s</strong></td></tr>
                  <tr><td style="padding:6px 0">Estado del pago</td><td>%s</td></tr>
                </table>
                <p style="margin-top:20px;color:#555"><strong>Dirección:</strong> %s<br><strong>Teléfono:</strong> %s</p>
              </div>
            </div>
            """.formatted(
                HtmlUtils.htmlEscape(nombre),
                c.tipoHabitacion(),
                c.habitacion(),
                c.fechaEntrada().format(FORMATO_FECHA),
                c.fechaSalida().format(FORMATO_FECHA),
                c.noches(),
                total,
                estadoPago,
                DIRECCION,
                TELEFONO);
    }
}
