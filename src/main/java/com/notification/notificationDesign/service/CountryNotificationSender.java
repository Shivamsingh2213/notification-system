package com.notification.notificationDesign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryNotificationSender {
    @Autowired
    private UaeSmsSender uaeSmsSender;
    @Autowired
    private UsaSmsSender usaSmsSender;
    @Autowired
    private IndiaSmsSender indiaSmsSender;

    public SmsSender getSmsSender(String country) throws IllegalAccessException {
        switch (country.toLowerCase()){
            case "uae":
                return uaeSmsSender;

            case "india":
                return indiaSmsSender;

            case "usa":
                return usaSmsSender;

            default:
                throw new IllegalAccessException("No SMS sender available for country:" + country);
        }
    }
}
