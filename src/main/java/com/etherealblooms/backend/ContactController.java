package com.etherealblooms.backend;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send-email")
    public Map<String, String> sendEmail(@RequestBody EmailRequest emailRequest) {
        log.info("Received email request from: {}", emailRequest.getFromName());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String sendToEmail = "seyadriyaz0@gmail.com"; 

            message.setFrom("your-sending-email@example.com"); 
            message.setTo(sendToEmail);
            message.setSubject("New Website Inquiry from " + emailRequest.getFromName());
            
            String emailBody = """
                You have a new inquiry:

                Name: %s
                Email: %s
                Mobile: %s

                Message:
                %s
                """.formatted(
                    emailRequest.getFromName(),
                    emailRequest.getFromEmail(),
                    emailRequest.getFromMobile(),
                    emailRequest.getMessage()
                );
            message.setText(emailBody);

            mailSender.send(message);

            log.info("Email successfully sent via mailSender to {}", sendToEmail);
            return Map.of("status", "success", "message", "Email sent successfully!");

        } catch (MailException e) {
            log.error("Failed to send email. Error: {}", e.getMessage()); 
            return Map.of("status", "error", "message", "Failed to send email.");
        }
    }
}

