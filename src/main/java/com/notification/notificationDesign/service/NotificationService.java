package com.notification.notificationDesign.service;

import com.notification.notificationDesign.constant.NotificationChannel;
import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;
import com.notification.notificationDesign.entities.Garage;
import com.notification.notificationDesign.entities.Notification;
import com.notification.notificationDesign.repository.CustomerRepository;
import com.notification.notificationDesign.repository.GarageRepository;
import com.notification.notificationDesign.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.notification.notificationDesign.constant.NotificationChannel.*;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private CountryNotificationSender countryNotificationSender;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private WhatsappSender whatsappSender;

    @Autowired
    private Map<String, SmsSender> smsSenders;

    @Async
    public void sendNotification(Long customerId, Long garageId, String message, NotificationType type, NotificationChannel channel) throws IllegalAccessException {
        Garage garage = garageRepository.findById(garageId).get();
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if ((channel == SMS && !customer.isSmsSubscribed()) || (channel == NotificationChannel.EMAIL && !customer.isEmailSubscribed()) || (channel == NotificationChannel.WHATSAPP && !customer.isWhatsappSubscribed())) {
                System.out.println("Alert!! Customer have UnSubscribed" + channel + "notification");
                return;
            }

            switch (channel) {
                case SMS -> {
                    SmsSender smsSender1 = countryNotificationSender.getSmsSender(customer.getCountry());
                    smsSender1.smsSender(customer, message, type);
                }
                case EMAIL -> emailSender.sendEmail(Optional.of(customer), message, type);
                case WHATSAPP -> whatsappSender.whatsappSender(Optional.of(customer), message, type);
            }

            Notification notification = new Notification();
            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage(message);
            notification.setNotificationType(type);
            notification.setChannel(channel);
            notification.setCustomer(customer);
            notification.setGarage(garage);

            notificationRepository.save(notification);
        } else {
            throw new IllegalAccessException("customer not found");
        }
    }

    @Async
    public void sendEmailNotification(Long customerId, Long garageId, String message, NotificationType type) throws IllegalAccessException {
        Garage garage = garageRepository.findById(garageId).get();
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (!customer.isEmailSubscribed()) {
                System.out.println("Alert!! Customer have UnSubscribed" + EMAIL + "notification");
                return;
            }
            emailSender.sendEmail(Optional.of(customer), message, type);
            Notification notification = new Notification();
            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage(message);
            notification.setNotificationType(type);
            notification.setChannel(EMAIL);
            notification.setCustomer(customer);
            notification.setGarage(garage);

            notificationRepository.save(notification);
        } else {
            throw new IllegalAccessException("customer not found");
        }

    }

    @Async
    public void sendSmsNotification(Notification notification) throws IllegalAccessException {
        Garage garage = garageRepository.findById(notification.getGarage().getId()).get();
        Customer customer = customerRepository.findById(notification.getCustomer().getId()).get();

        if (!customer.isSmsSubscribed()) {
            System.out.println("Alert!! Customer have UnSubscribed" + SMS + "notification");
            return;
        }
        SmsSender smsSender1 = countryNotificationSender.getSmsSender(customer.getCountry());
        smsSender1.smsSender(customer, notification.getMessage(), notification.getNotificationType());

        Notification notification1 = new Notification();
        notification1.setCreatedAt(LocalDateTime.now());
        notification1.setMessage(notification.getMessage());
        notification1.setNotificationType(notification.getNotificationType());
        notification1.setChannel(SMS);
        notification1.setCustomer(customer);
        notification1.setGarage(garage);

        notificationRepository.save(notification1);


    }

    @Async
    public void sendWhatsappNotification(Notification notification) {
        Garage garage = garageRepository.findById(notification.getGarage().getId()).get();
        Customer customer = customerRepository.findById(notification.getCustomer().getId()).get();
        if (!customer.isWhatsappSubscribed()) {
            System.out.println("Alert!! Customer have UnSubscribed" + WHATSAPP + "notification");
            return;
        }
        whatsappSender.whatsappSender(Optional.of(customer), notification.getMessage(), notification.getNotificationType());

        Notification notification1 = new Notification();
        notification1.setCreatedAt(LocalDateTime.now());
        notification1.setMessage(notification.getMessage());
        notification1.setNotificationType(notification.getNotificationType());
        notification1.setChannel(WHATSAPP);
        notification1.setCustomer(customer);
        notification1.setGarage(garage);

        notificationRepository.save(notification1);

    }


    public List<Notification> getMessage(String mobileNo) {
        Customer user = customerRepository.findByMobileNo(mobileNo);
        if (user == null) {
            System.out.println("Customer not found for mobile number: " + mobileNo);
            return Collections.emptyList();
        }

        List<Notification> notifications = notificationRepository.findNotificationByCustomerId(user.getId());

        if (notifications.isEmpty()) {
            System.out.println("No messages found for customer ID: " + user.getId());
        }

        return notifications;
    }


    public List<String> msgById(long id) {
        return notificationRepository.findMessageByCustomerId(id);
    }


    public List<String> getByCriteria(Long customerId, LocalDateTime fromDate, LocalDateTime toDate, NotificationType notificationType) {
        return notificationRepository.findMessagesByCriteria(customerId, fromDate, toDate, notificationType != null ? notificationType.name() : null);
    }


    @Async
    public String unsubscribe(Long customerId, NotificationChannel channel) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return "Customer not found.";
        }

        Customer customer = customerOpt.get();
        // Unsubscribe based on the channel
        switch (channel) {
            case SMS -> customer.setSmsSubscribed(false);
            case EMAIL -> customer.setEmailSubscribed(false);
            case WHATSAPP -> customer.setWhatsappSubscribed(false);
            default -> {
                return "Invalid channel.";
            }
        }
        customerRepository.save(customer);
        return "Customer unsubscribed from " + channel + " notifications.";
    }

    public Page<Notification> getNotifications(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return notificationRepository.findAllSorted(pageable);
    }

}

