package com.notification.notificationDesign.repository;

import com.notification.notificationDesign.entities.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GarageRepository extends JpaRepository<Garage,Long> {
}
