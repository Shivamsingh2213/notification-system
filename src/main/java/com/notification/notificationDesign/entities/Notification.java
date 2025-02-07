package com.notification.notificationDesign.entities;

import com.notification.notificationDesign.constant.NotificationChannel;
import com.notification.notificationDesign.constant.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String channels;


    private String message;
    private LocalDateTime createdAt = LocalDateTime.now();
}
