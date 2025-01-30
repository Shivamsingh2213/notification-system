package com.notification.notificationDesign.controller;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Notification;
import com.notification.notificationDesign.service.NotificationService;
import org.hibernate.jpa.internal.util.PessimisticNumberParser;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> sendNotification(@RequestBody Notification notification) {
        notificationService.sendNotification(
                notification.getGarage().getId(),
                notification.getCustomer().getId(),
                notification.getNotificationType(),
                notification.getMessage());
        return ResponseEntity.ok("Notification sent successfully");
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<String>> getMessageByMobileNo(@RequestParam(value = "mobileNo") String mobileNo) {
        List<String> msg = notificationService.getMessage(mobileNo);
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
            @RequestParam(required = false, value = "customer_id") Long id,
            @RequestParam(required = false, value = "created_at")  LocalDateTime date,
            @RequestParam(required = false, value = "notification_type") NotificationType type) {
        List<String> msg = notificationService.getByCriteria(id, date, type);
        return ResponseEntity.ok(msg);

    }
    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam Long customerId) {
        return ResponseEntity.ok(notificationService.unsubscribe(customerId));
    }
}
