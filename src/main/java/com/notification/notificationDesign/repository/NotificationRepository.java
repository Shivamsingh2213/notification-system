package com.notification.notificationDesign.repository;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
 long countByNotificationType(NotificationType notificationType);

 @Query("SELECT n.message FROM Notification n WHERE n.customer.id = :customerId")
 List<String> findMessageByCustomerId(@Param("customerId") long customerId);

 @Query(value = "SELECT u.message FROM notification u WHERE " +
         "(:customer_id IS NULL OR u.customer_id = :customer_id) AND " +
         "(:created_at IS NULL OR u.created_at = :created_at) AND " +
         "(:notification_type IS NULL OR u.notification_type = :notification_type)",
         nativeQuery = true)
 List<String> findMessagesByIdOrDateOrType(@Param("customer_id") Long customerId,
                                           @Param("created_at") LocalDateTime createdAt,
                                           @Param("notification_type") String notificationType);
}

