package com.notification.notificationDesign.service;

import com.notification.notificationDesign.entities.EmailData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service

public class EmailSender {
    private final RestTemplate restTemplate;
    private static final String URL = "https://65bb27af52189914b5bb5326.mockapi.io/api/v1/create";


    public EmailSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmailData sendEmail(EmailData emailData) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<EmailData> requestEntity = new HttpEntity<>(emailData, headers);

            ResponseEntity<EmailData> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    requestEntity,
                    EmailData.class
            );

            return response.getBody();

    }
}
