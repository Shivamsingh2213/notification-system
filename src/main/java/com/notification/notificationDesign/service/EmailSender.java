package com.notification.notificationDesign.service;

import com.notification.notificationDesign.entities.EmailData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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

        // return response.getBody();
        EmailData responseData = response.getBody();

        if (responseData != null) {
            System.out.println("Email sent successfully. Received ID: " + responseData.getId());
        } else {
            System.out.println("Failed to send email. No ID received.");
        }

        return responseData;

    }

//    public List<EmailData> getAllEmails() {
//        ResponseEntity<EmailData[]> response = restTemplate.exchange(
//                URL,
//                HttpMethod.GET,
//                null,
//                EmailData[].class
//        );
//
//        return List.of(response.getBody());
//    }
//
//
//    public EmailData getEmailById(String id) {
//        String emailUrl = URL + "/" + id;
//        ResponseEntity<EmailData> response = restTemplate.exchange(
//                emailUrl,
//                HttpMethod.GET,
//                null,
//                EmailData.class
//        );
//
//        return response.getBody();
//    }

    public Object getEmails(String id) {

        if (id == null || id.isEmpty()) {
            ResponseEntity<EmailData[]> response = restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    null,
                    EmailData[].class
            );

            EmailData[] emails = response.getBody();
            if (emails == null || emails.length == 0) {
                System.out.println("No emails found.");
                return List.of();
            }
            System.out.println("Fetched all emails: " + Arrays.toString(emails));
            return Arrays.asList(emails);
        } else {
            ResponseEntity<EmailData> response = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.GET,
                    null,
                    EmailData.class
            );

            EmailData email = response.getBody();
            if (email == null) {
                System.out.println("No email found for ID: " + id);
                return null;
            }
            System.out.println("Fetched email with ID " + id + ": " + email);
            return email;
        }

    }
}
