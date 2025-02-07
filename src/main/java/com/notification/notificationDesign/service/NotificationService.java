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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private EnumSet<NotificationChannel> channels; // or List<NotificationChannel>

    @Async
    public void sendNotification(Long customerId, Long garageId, String message, NotificationType type, EnumSet<NotificationChannel> channels) throws IllegalAccessException {
        Garage garage = garageRepository.findById(garageId).orElseThrow(() -> new IllegalAccessException("Garage not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new IllegalAccessException("Customer not found"));

        EnumSet<NotificationChannel> unsubscribedChannels = EnumSet.noneOf(NotificationChannel.class);

        if (channels.contains(NotificationChannel.SMS) && !customer.isSmsSubscribed()) {
            unsubscribedChannels.add(NotificationChannel.SMS);
        }
        if (channels.contains(NotificationChannel.EMAIL) && !customer.isEmailSubscribed()) {
            unsubscribedChannels.add(NotificationChannel.EMAIL);
        }
        if (channels.contains(NotificationChannel.WHATSAPP) && !customer.isWhatsappSubscribed()) {
            unsubscribedChannels.add(NotificationChannel.WHATSAPP);
        }

        if (!unsubscribedChannels.isEmpty()) {
            System.out.println("Alert!! Customer has unsubscribed from " + unsubscribedChannels);
            return;
        }

        // Send notifications for each channel
//        for (NotificationChannel channel : channels) {
//            switch (channel) {
//                case SMS -> {
//                    SmsSender smsSender = countryNotificationSender.getSmsSender(customer.getCountry());
//                    smsSender.smsSender(customer, message, type);
//                }
//                case EMAIL -> emailSender.sendEmail(Optional.of(customer), message, type);
//                case WHATSAPP -> whatsappSender.whatsappSender(Optional.of(customer), message, type);
//            }
//        }

        // Store the notification in the database
        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(message);
        notification.setNotificationType(type);
        String channelsString = channels.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        notification.setChannels(channelsString);  // This should be correct if channels is of type EnumSet<NotificationChannel>
        notification.setCustomer(customer);
        notification.setGarage(garage);

        notificationRepository.save(notification);
    }

    public List<Notification> getMessage(String mobileNo, NotificationType type, int pageNumber, int pageSize) {
        Customer user = customerRepository.findByMobileNo(mobileNo);
        if (user == null) {
            System.out.println("Customer not found for mobile number: " + mobileNo);
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Notification> notificationsPage = notificationRepository.findNotificationByCustomerIdAndNotificationType(user.getId(), type, pageable);

        if (notificationsPage.isEmpty()) {
            System.out.println("No messages found for customer ID: " + user.getId());
        }

        return notificationsPage.getContent();
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

    public Map<Long, Notification> getNotifications(int customerId) {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
                .filter(n -> n.getCustomer().getId() == customerId)
                .collect(Collectors.toMap(Notification::getId, Function.identity()));
    }
}