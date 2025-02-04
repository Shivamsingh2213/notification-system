package com.notification.notificationDesign.controller;

import com.notification.notificationDesign.constant.NotificationChannel;
import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Notification;
import com.notification.notificationDesign.service.NotificationService;
import org.hibernate.jpa.internal.util.PessimisticNumberParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Notification notification) throws Exception {
        notificationService.sendNotification(notification.getCustomer().getId(),notification.getGarage().getId(),notification.getMessage(),notification.getNotificationType(),notification.getChannel());
        return ResponseEntity.ok("Notification sent successfully");
    }

    @PostMapping("/send/email")
    public ResponseEntity<String> sendEmailNotification (@RequestBody Notification notification) throws IllegalAccessException {
         notificationService.sendEmailNotification(notification.getCustomer().getId(),notification.getGarage().getId(),notification.getMessage(),notification.getNotificationType());
        return ResponseEntity.ok("Notification sent successfully through Email");
    }
    @PostMapping("/send/sms")
    public ResponseEntity<String> sendSmsNotification(@RequestBody Notification notification) throws IllegalAccessException {
         notificationService.sendSmsNotification(notification);
         return ResponseEntity.ok("Notification sent successfully through SMS");
    }

    @PostMapping("/send/whatsapp")
    public ResponseEntity<String> sendWhatsappNotification(@RequestBody Notification notification) {
        notificationService.sendWhatsappNotification(notification);
        return ResponseEntity.ok("Notification send successfully though whatsapp");
    }


    @GetMapping("/fetch")
    public ResponseEntity<List<Notification>> getNotificationByMobileNo(@RequestParam(value = "mobileNo") String mobileNo) {
        List<Notification> msg = notificationService.getMessage(mobileNo);
        if (msg == null || msg.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(msg);
    }


    @GetMapping("/byId")
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

    @GetMapping
    public ResponseEntity<Page<Notification>> getNotifications(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {

        Page<Notification> notifications = notificationService.getNotifications(pageNo, pageSize);
        return ResponseEntity.ok(notifications);
    }

}
