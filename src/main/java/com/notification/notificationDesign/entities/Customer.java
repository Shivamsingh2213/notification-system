package com.notification.notificationDesign.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String mobileNo;
    private String name;
    private String whatsappNo;
    @Column(columnDefinition = "BOOLEAN")
    private boolean smsSubscribed = false;

    @Column(columnDefinition = "BOOLEAN")
    private boolean emailSubscribed = true;

    @Column(columnDefinition = "BOOLEAN")
    private boolean whatsappSubscribed = true;
    private String country;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notification> notifications;
}
