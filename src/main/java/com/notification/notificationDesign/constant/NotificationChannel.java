package com.notification.notificationDesign.constant;

public enum NotificationChannel {
    SMS,
    EMAIL,
    WHATSAPP;


    public String toUpperCase() {
        return name().toUpperCase();
    }
}
