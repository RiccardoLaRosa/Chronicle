package it.aulab.chronicle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendSimpleEmail(String to, String subjet, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("amministrator@chronicle.com");
        message.setTo(to);
        message.setSubject(subjet);
        message.setText(text);
        mailSender.send(message);
    }
    
}
