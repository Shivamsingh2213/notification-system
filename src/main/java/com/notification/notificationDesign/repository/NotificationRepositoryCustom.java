package com.notification.notificationDesign.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepositoryCustom {
    List<String> findMessagesByCriteria(Long customerId, LocalDateTime fromDate, LocalDateTime toDate, String notificationType);
}



