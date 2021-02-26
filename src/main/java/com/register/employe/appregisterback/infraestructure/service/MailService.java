package com.register.employe.appregisterback.infraestructure.service;

import com.register.employe.appregisterback.domain.exception.CorreoActivacionException;
import com.register.employe.appregisterback.infraestructure.model.NotificacionEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificacionEmail notificacionEmail) throws CorreoActivacionException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            messageHelper.setFrom("devfelipefranco@gmial.com");
            messageHelper.setTo(notificacionEmail.getDestinatario());
            messageHelper.setSubject(notificacionEmail.getAsunto());
            messageHelper.setText(mailContentBuilder.build(notificacionEmail.getCuerpo()), true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Se envio el email para la activacion de la cuenta");
        } catch (MailException e) {
            log.error("Excepcion enviando email de activacion");
            throw new CorreoActivacionException("Ocurrio una excepcion cuando se envio el correo a " + notificacionEmail.getDestinatario(), e);
        }
    }
}
