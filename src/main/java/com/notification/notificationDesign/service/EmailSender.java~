package com.notification.notificationDesign.service;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailSender {
    public void sendEmail(Optional<Customer> customer, String message, NotificationType type){
        System.out.println(type + " Email sent to " + customer.get().getEmail()+":"+message);

    }
}
