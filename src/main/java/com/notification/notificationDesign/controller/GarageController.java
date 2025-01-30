package com.notification.notificationDesign.controller;

import com.notification.notificationDesign.entities.Garage;
import com.notification.notificationDesign.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/garage")

public class GarageController {
    @Autowired
    private GarageService garageService;

    @PostMapping
    public ResponseEntity<String> addgarage(@RequestBody Garage garage){
        garageService.addGarage(garage);
        return ResponseEntity.ok("garage added");
    }
}
