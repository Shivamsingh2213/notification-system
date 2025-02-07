package com.notification.notificationDesign.repository;

import com.notification.notificationDesign.constant.NotificationType;
import com.notification.notificationDesign.entities.Notification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>, NotificationRepositoryCustom {
 long countByNotificationType(NotificationType notificationType);


 @Query("SELECT n FROM Notification n ORDER BY CASE " +
         "WHEN n.notificationType = 'PROMOTIONAL' THEN 1 " +
         "WHEN n.notificationType = 'TRANSACTIONAL' THEN 2 END ASC")
 Page<Notification> findAllSorted(Pageable pageable);

 @Query("SELECT n.message FROM Notification n WHERE n.customer.id = :customerId")
 List<String> findMessageByCustomerId(@Param("customerId") long customerId);

 @Query("SELECT n FROM Notification n WHERE n.customer.id = :customerId AND n.notificationType = :notificationType")
 Page<Notification> findNotificationByCustomerIdAndNotificationType(
         @Param("customerId") Long customerId,
         @Param("notificationType") NotificationType notificationType,
         Pageable pageable
 );



// @Query(value = "SELECT u.message FROM notification u WHERE " +
//         "(:customer_id IS NULL OR u.customer_id = :customer_id) AND " +
//         "(:notification_type IS NULL OR u.notification_type = :notification_type) AND " +
//         "(:fromDate IS NULL OR :toDate IS NULL OR u.created_at BETWEEN :fromDate AND :toDate)",
//         nativeQuery = true)
// List<String> findMessagesByCriteria(@Param("customer_id") Long customerId,
//                                     @Param("fromDate") LocalDateTime fromDate,
//                                     @Param("toDate") LocalDateTime toDate,
//                                     @Param("notification_type") String notificationType);




}

