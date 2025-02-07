package com.notification.notificationDesign.controller;

import com.notification.notificationDesign.constant.NotificationChannel;
import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Customer;
import com.notification.notificationDesign.entities.EmailData;
import com.notification.notificationDesign.entities.Notification;
import com.notification.notificationDesign.service.CustomerService;
import com.notification.notificationDesign.service.EmailSender;
import com.notification.notificationDesign.service.NotificationService;
import org.hibernate.jpa.internal.util.PessimisticNumberParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private CustomerService customerService;


@Async
 @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailData emailData) {
        String response = emailSender.sendEmail(emailData);
        return ResponseEntity.ok(response);
    }


//    @Async
//    @PostMapping("/send")
//    public ResponseEntity<String> sendNotification(@RequestBody Notification request) throws Exception {
//        // Convert List<String> to EnumSet<NotificationChannel>
//        EnumSet<NotificationChannel> channels = request.getChannels().stream()
//                .map(channel -> {
//                    try {
//                        return NotificationChannel.valueOf(channel.toUpperCase());
//                    } catch (IllegalArgumentException e) {
//                        throw new RuntimeException("Invalid channel: " + channel);
//                    }
//                })
//                .collect(Collectors.toCollection(() -> EnumSet.noneOf(NotificationChannel.class)));
//
//        notificationService.sendNotification(
//                request.getCustomer().getId(),
//                request.getGarage().getId(),
//                request.getMessage(),
//                request.getNotificationType(),
//                channels
//        );
//
//        return ResponseEntity.ok("Notification sent successfully!");
//    }




    @GetMapping("/notification-by-TypeAndMobileNo")
    public ResponseEntity<List<Notification>> getNotificationByMobileNoAndType(
            @RequestParam(value = "mobileNo") String mobileNo,
            @RequestParam(value = "type") NotificationType type,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize
    ) {
        // Fetch paginated notifications from service
        List<Notification> msg = notificationService.getMessage(mobileNo, type, pageNumber, pageSize);

        if (msg == null || msg.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(msg);
    }



    @GetMapping("/notification-byUserId")
    public ResponseEntity<List<String>> getMessageByUserId(@RequestParam(value = "customer_id") Long id) {
        List<String> msg = notificationService.msgById(id);
        return ResponseEntity.ok(msg);
    }


    @GetMapping("by_anything")
    public ResponseEntity<List<String>> getMessageByCriteria(
            @RequestParam(required = false, value = "customer_id") Long customerId,
            @RequestParam(required = false, value = "from_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false, value = "to_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false, value = "notification_type") NotificationType type) {

        List<String> msg = notificationService.getByCriteria(customerId, fromDate, toDate, type);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam Long customerId, @RequestParam NotificationChannel channel) {
        return notificationService.unsubscribe(customerId, channel);
    }

    @GetMapping("/fetch-By-Pages")
    public ResponseEntity<Page<Notification>> getNotifications(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {

        Page<Notification> notifications = notificationService.getNotifications(pageNo, pageSize);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notification/stream")
    public ResponseEntity<Map<Long,Notification>> getNotifications(@RequestParam Long customerId)
    {
        Map<Long,Notification> notifications = notificationService.getNotifications(Math.toIntExact(customerId));
        return ResponseEntity.ok(notifications);
    }




}
