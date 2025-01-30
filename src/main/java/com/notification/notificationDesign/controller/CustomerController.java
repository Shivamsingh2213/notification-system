package com.notification.notificationDesign.controller;

import com.notification.notificationDesign.entities.Customer;
import com.notification.notificationDesign.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping
     public ResponseEntity<String> addCustomer(@RequestBody Customer customer){
         customerService.addCustomer(customer);
         return ResponseEntity.ok("customer added");
     }
}
