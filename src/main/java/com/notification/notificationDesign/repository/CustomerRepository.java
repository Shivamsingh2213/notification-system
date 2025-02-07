package com.notification.notificationDesign.repository;

import com.notification.notificationDesign.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByMobileNo(@Param(value="mobile_no") String mobileNo);

    Customer findByEmail(@Param(value="email") String email);
}
