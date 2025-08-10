package com.rasel.security_demo.Repository;

import com.rasel.security_demo.model.Order;
import com.rasel.security_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order , Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByUser(User user);
}
