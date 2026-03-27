package com.foxwear.notificationservice.service;

import com.foxwear.common.event.PasswordResetEvent;
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

            String header = "Confirm Your Email Address";
            String text = "Welcome to the FoxWear community! We're excited to have you on board. To get started, please verify your account by clicking the button below.";
            String buttonText = "Verify Your Email";

            String htmlContent = buildHtmlVerificationEmail(header, text, event.getLink(), buttonText);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendPasswordResetEmail(PasswordResetEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("FoxWear - Password Reset Request");

            String header = "Reset Your Password";
            String text = "Welcome back to the FoxWear community! Use the link below to reset your password. If you did not send this, please ignore it.";
            String buttonText = "Reset Password";

            String htmlContent = buildHtmlVerificationEmail(header, text, event.getLink(), buttonText);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendPasswordResetInfoEmail(String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Security Alert: Your FoxWear Password Has Been Changed");

            String htmlContent = buildHtmlPasswordResetEmail();
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildHtmlVerificationEmail(String header, String text, String link, String buttonText) {
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
                "      <h2>" + header + "</h2>" +
                "      <p>" + text + "</p>" +
                "      <a href='" + link + "' class='button'>" + buttonText + "</a>" +
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

    private String buildHtmlPasswordResetEmail() {
        return "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; padding: 30px; border: 1px solid #f0f0f0; border-radius: 12px; background-color: #ffffff;\">" +
                "<div style=\"text-align: center; margin-bottom: 25px;\">" +
                "<h1 style=\"color: #1a73e8; margin: 0;\">FoxWear</h1>" +
                "</div>" +
                "<h2 style=\"color: #202124; font-size: 22px; text-align: center; margin-bottom: 20px;\">Password Changed Successfully</h2>" +
                "<p style=\"color: #3c4043; font-size: 16px; line-height: 1.5;\">Hello,</p>" +
                "<p style=\"color: #3c4043; font-size: 16px; line-height: 1.5;\">This is a confirmation that the password for your <strong>FoxWear</strong> account has been recently changed.</p>" +

                "<div style=\"background-color: #fce8e6; padding: 20px; border-radius: 8px; border-left: 6px solid #d93025; margin: 25px 0;\">" +
                "<p style=\"margin: 0; color: #a50e0e; font-weight: bold; font-size: 14px;\">If you did not make this change:</p>" +
                "<p style=\"margin: 10px 0 0 0; color: #a50e0e; font-size: 14px;\">Please contact our support team immediately or reset your password again to secure your account. Your account security is our top priority.</p>" +
                "</div>" +

                "<p style=\"color: #70757a; font-size: 14px; text-align: center; margin-top: 30px;\">If this was you, you can safely disregard this email.</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #eeeeee; margin: 30px 0;\">" +
                "<footer style=\"text-align: center; color: #9aa0a6; font-size: 12px;\">" +
                "&copy; 2026 FoxWear Security Team<br>Azerbaijan, Baku" +
                "</footer>" +
                "</div>";
    }


}
