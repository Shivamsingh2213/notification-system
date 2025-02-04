package com.notification.notificationDesign.service;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class WhatsappSender {
    public void whatsappSender(Optional<Customer> customer, String message, NotificationType type){
        System.out.println(type + " Whatsapp notification send to" + customer.get().getWhatsappNo()+":"+message);
    }
}
