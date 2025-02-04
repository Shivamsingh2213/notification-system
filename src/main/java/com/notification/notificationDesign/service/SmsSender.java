package com.notification.notificationDesign.service;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;

import java.util.Optional;

public interface SmsSender {
    public void smsSender(Customer customer, String message, NotificationType type);
}
