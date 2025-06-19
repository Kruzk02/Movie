package com.app.module.user.service.impl;

import com.app.module.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Mono<Void> sendVerificationEmail(String to, String token) {
        return Mono.fromRunnable(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject("Verify Your Account");

                String htmlContent = "<html>" +
                        "<body>" +
                        "<h1>Verify Your Account</h1>" +
                        "<p>To verify your account, click on the following link:</p>" +
                        "<a href=\"http://localhost:8080/users/verify?token=" + token + "\">Verify Now</a>" +
                        "</body>" +
                        "</html>";

                helper.setText(htmlContent, true);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email", e);
            }
            javaMailSender.send(mimeMessage);
        }).then();
    }
}
