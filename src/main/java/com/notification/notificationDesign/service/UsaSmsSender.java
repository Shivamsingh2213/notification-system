package com.notification.notificationDesign.service;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("usaSmsSender")
public class UsaSmsSender implements SmsSender {
    @Override
    public void smsSender(Customer customer, String message, NotificationType  type){
        System.out.println(type + " SMS sent via USA provider to " + customer.getMobileNo() + ": " + message);
    }

}
