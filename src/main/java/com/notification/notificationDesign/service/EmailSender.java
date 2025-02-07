package com.notification.notificationDesign.service;

import com.notification.notificationDesign.entities.EmailData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class EmailSender {
    private final RestTemplate restTemplate;
    private static final String URL = "https://67a4a4c9c0ac39787a1bfd9b.mockapi.io/message";


    public EmailSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendEmail(EmailData emailData) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<EmailData> requestEntity = new HttpEntity<>(emailData, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return response.getBody();

    }
}
