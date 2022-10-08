package com.emailmicroservice.service;

import com.emailmicroservice.models.DrinkModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "drinks", groupId = "group1")
    public void sendEmail(String drinkMessage) throws MessagingException, JsonProcessingException {
        log.info("Received new Message...");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMessage = "<h3> Something happened! </h3>" +
                "<p>" + drinkMessage + "</p>";
        helper.setFrom("testes.java.forttiori@gmail.com");
        helper.setTo("testes.java.forttiori@gmail.com");
        helper.setSubject("Drink Event");
        helper.setText(htmlMessage, true);
        javaMailSender.send(mimeMessage);
        log.info("Email sent successfully");
    }

}
