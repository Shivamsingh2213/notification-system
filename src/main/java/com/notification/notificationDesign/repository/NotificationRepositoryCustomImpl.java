package com.notification.notificationDesign.repository;

import com.notification.notificationDesign.entities.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> findMessagesByCriteria(Long customerId, LocalDateTime fromDate, LocalDateTime toDate, String notificationType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<Notification> root = query.from(Notification.class);

        query.select(root.get("message"));

        List<Predicate> predicates = new ArrayList<>();

        if (customerId != null) {
            try {
                root.get("customerId");
                predicates.add(cb.equal(root.get("customerId"), customerId));
            } catch (IllegalArgumentException e) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }
        }

        if (notificationType != null) {
            predicates.add(cb.equal(root.get("notificationType"), notificationType));
        }

        if (fromDate != null && toDate != null) {
            predicates.add(cb.between(root.get("createdAt"), fromDate, toDate));
        }

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<String> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
