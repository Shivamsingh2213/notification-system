package com.notification.notificationDesign.service;

import com.notification.notificationDesign.entities.Garage;
import com.notification.notificationDesign.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GarageService {
    @Autowired
    private GarageRepository garageRepository;

    public void addGarage(Garage garage){
       garageRepository.save(garage);
    }
}
