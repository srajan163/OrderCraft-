package com.sboot.service.MailService;

import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ByteArrayResource;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.ByteArrayInputStream;

import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String username, String rawPassword) {
        String subject = "Welcome to OrderCraft!";
        String message = String.format(
                "Hi %s,\n\nYour account has been successfully created.\n\nUsername: %s\nPassword: %s\n\nPlease log in and change your password as soon as possible.\n\nRegards,\nOrderCraft Team",
                username, username, rawPassword
        );

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);
    }
    
    
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    
    public void OtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateAndStoreOtp(String email) {
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        otpStorage.put(email, otp);
        sendOtpEmail(email, otp);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String expectedOtp = otpStorage.get(email);
        if (expectedOtp == null || otp == null) {
            return false;
        }
        return expectedOtp.equals(otp);
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\n\nPlease use this to reset your password. The code will expire shortly.");
        mailSender.send(message);
    }
    
    
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@yourdomain.com"); // or your configured email
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    public void sendInvoiceEmail(String from, String to, String subject, String htmlBody, String attachmentFilename, byte[] attachmentBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // MULTIPART_MODE_MIXED_RELATED is necessary for attachment + html
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(from != null ? from : "noreply@ordercraft.com");
            helper.setTo(to);
            helper.setSubject(subject != null ? subject : "No Subject");

            // Handle missing body
            if (htmlBody == null || htmlBody.trim().isEmpty()) {
                htmlBody = "<p>No content provided</p>";
            }

            // Provide plain-text fallback by stripping HTML tags
            String plainTextBody = htmlBody.replaceAll("<[^>]*>", "");

            // Set both plain text and HTML body
            helper.setText(plainTextBody, htmlBody);

            // Attach PDF if available
            if (attachmentBytes != null && attachmentBytes.length > 0) {
                ByteArrayResource resource = new ByteArrayResource(attachmentBytes);
                helper.addAttachment(
                        attachmentFilename != null ? attachmentFilename : "invoice.pdf",
                        resource
                );
            }

            mailSender.send(message);
            System.out.println("✅ Email with invoice sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
    public void sendEmailWithAttachmentForPapercut(
            String from,
            String to,
            String subject,
            String htmlBody,
            String attachmentFilename,
            byte[] attachmentBytes) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart email
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(from != null ? from : "noreply@yourdomain.com");
            helper.setTo(to);
            helper.setSubject(subject != null ? subject : "(No Subject)");

            // Fallback plain text and HTML body
            String fallbackPlainText = htmlBody != null ? htmlBody.replaceAll("<[^>]*>", "") : "No content.";
            helper.setText(fallbackPlainText, htmlBody); // plain + html

            // Add attachment
            if (attachmentBytes != null && attachmentBytes.length > 0) {
                ByteArrayResource resource = new ByteArrayResource(attachmentBytes);
                helper.addAttachment(
                    attachmentFilename != null ? attachmentFilename : "invoice.pdf",
                    resource
                );
            } else {
                System.out.println("No attachment included.");
            }

            mailSender.send(message);
            System.out.println("✅ Email sent (check PaperCut).");

        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

//
//public void sendEmailWithAttachment(String from, String to, String subject, String messageBody,
//                                     String fileName, byte[] pdf) {
//    try {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//        helper.setFrom(from);
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(messageBody);
//
//        // Add PDF as attachment
//        helper.addAttachment(fileName, new ByteArrayResource(pdf));
//
//        mailSender.send(mimeMessage);
//    } catch (MessagingException e) {
//        // Log or handle the exception as needed
//        System.err.println("Failed to send email with attachment: " + e.getMessage());
//        throw new RuntimeException("Failed to send email with attachment", e);
//    }
//}    
    

//  
//    public void sendEmailWithAttachmentAlt(String from, String to, String subject, String htmlBody,
////            String attachmentFilename, byte[] attachmentBytes) {
////try {
////if (subject == null || subject.trim().isEmpty()) {
////subject = "(No Subject)";
////}
////
////if (htmlBody == null || htmlBody.trim().isEmpty()) {
////htmlBody = "<p>(No message body provided)</p>";
////}
////
////MimeMessage message = mailSender.createMimeMessage();
////message.setFrom(new InternetAddress(from != null ? from : "noreply@yourdomain.com"));
////message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
////message.setSubject(subject, StandardCharsets.UTF_8.name());
////
////// Create multipart container
////Multipart multipart = new MimeMultipart();
////
////// Part 1: HTML body part
////MimeBodyPart htmlPart = new MimeBodyPart();
////htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
////multipart.addBodyPart(htmlPart);
////
////// Part 2: Attachment part
////if (attachmentBytes != null && attachmentBytes.length > 0) {
////MimeBodyPart attachmentPart = new MimeBodyPart();
////ByteArrayDataSource dataSource = new ByteArrayDataSource(
////new ByteArrayInputStream(attachmentBytes),
////"application/pdf"
////);
////attachmentPart.setDataHandler(new DataHandler(dataSource));
////attachmentPart.setFileName(attachmentFilename != null ? attachmentFilename : "invoice.pdf");
////multipart.addBodyPart(attachmentPart);
////}
////
////// Set multipart content to message
////message.setContent(multipart);
////
////// Send the email
////mailSender.send(message);
////System.out.println("✅ Email sent successfully to " + to);
////} catch (Exception e) {
////System.err.println("❌ Failed to send email: " + e.getMessage());
////throw new RuntimeException("Error sending email", e);
////}
////}
////

}
