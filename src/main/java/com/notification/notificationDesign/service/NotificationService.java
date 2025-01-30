package com.notification.notificationDesign.service;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;
import com.notification.notificationDesign.entities.Garage;
import com.notification.notificationDesign.entities.Notification;
import com.notification.notificationDesign.repository.CustomerRepository;
import com.notification.notificationDesign.repository.GarageRepository;
import com.notification.notificationDesign.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GarageRepository garageRepository;

    public void sendNotification(Long garageId, Long customerId, NotificationType type, String message) {
        Garage garage = garageRepository.findById(garageId).get();
        Customer customer = customerRepository.findById(customerId).get();

        Notification notification = new Notification();
        notification.setGarage(garage);
        notification.setCustomer(customer);
        notification.setNotificationType(type);
        notification.setMessage(message);
        notificationRepository.save(notification);

        if (type == NotificationType.TRANSACTIONAL) {
            System.out.println("Transactional Email sent to: " + customer.getEmail());
            System.out.println("Transactional SMS sent to: " + customer.getMobileNo());
        } else {
            System.out.println("Promotional Email sent to: " + customer.getEmail());
            System.out.println("Promotional SMS sent to: " + customer.getMobileNo());
        }

    }


    public List<String> getMessage(String mobileNo) {
        Customer user = customerRepository.findByMobileNo(mobileNo);
        if (user == null) {
            System.out.println("Customer not found for mobile number: " + mobileNo);
            return Collections.emptyList(); // Return an empty list if customer is not found
        }

        Optional<List<String>> optionalUser = Optional.ofNullable(notificationRepository.findMessageByCustomerId(user.getId()));
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            System.out.println("No messages found for customer ID: " + user.getId());
            return Collections.emptyList();
        }
    }


    public List<String> msgById(long id) {
        return notificationRepository.findMessageByCustomerId(id);
    }


//    public List<String> getByCriteria(Long id, LocalDateTime date, NotificationType notificationType) {

    /// /        String notificationTypeStr = (notificationType != null) ? notificationType.name() : null;
    /// /        System.out.println("Parameters: id=" + id + ", date=" + date + ", notificationType=" + notificationTypeStr);
//        return notificationRepository.findMessagesByIdOrDateOrType(id, date, String.valueOf(notificationType));
//    }
    public List<String> getByCriteria(Long id, LocalDateTime date, NotificationType notificationType) {
        return notificationRepository.findMessagesByIdOrDateOrType(id, date, notificationType);
    }


    public String unsubscribe(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return "Customer not found.";
        }
        Customer customer = customerOpt.get();
        customer.setSubscribed(false);
        customerRepository.save(customer);
        return "Customer unsubscribed.";
    }

}
























