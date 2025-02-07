package com.notification.notificationDesign.service;

import com.notification.notificationDesign.entities.Customer;
import com.notification.notificationDesign.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void addCustomer(Customer customer){
         customerRepository.save(customer);
    }

    public Customer getCustomerByEmail(String email){
       return customerRepository.findByEmail(email);
    }

}
