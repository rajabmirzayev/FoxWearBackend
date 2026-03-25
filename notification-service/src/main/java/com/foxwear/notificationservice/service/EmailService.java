package com.foxwear.notificationservice.service;

import com.foxwear.common.event.RegistrationEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendRegistrationEmail(RegistrationEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Welcome to FoxWear - Verify Your Account");

            String htmlContent = buildHtmlEmail(event.getLink());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildHtmlEmail(String link) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <style>" +
                "    .email-container { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; }" +
                "    .header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #49352c; }" +
                "    .content { padding: 30px 20px; text-align: center; }" +
                "    .button { background-color: #49352c; color: #ffffff !important; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block; margin-top: 20px; }" +
                "    .footer { font-size: 12px; text-align: center; color: #888; margin-top: 30px; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='email-container'>" +
                "    <div class='header'>" +
                "      <h1>FoxWear</h1>" +
                "    </div>" +
                "    <div class='content'>" +
                "      <h2>Confirm Your Email Address</h2>" +
                "      <p>Welcome to the FoxWear community! We're excited to have you on board. To get started, please verify your account by clicking the button below.</p>" +
                "      <a href='" + link + "' class='button'>Verify Your Email</a>" +
                "      <p style='margin-top: 25px;'>If the button doesn't work, you can copy and paste the following link into your browser:</p>" +
                "      <p style='font-size: 11px; color: #888;'>" + link + "</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "      &copy; 2026 FoxWear Inc. All rights reserved.<br>" +
                "      Baku, Azerbaijan" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}
