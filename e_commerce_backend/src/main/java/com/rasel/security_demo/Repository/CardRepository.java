package com.rasel.security_demo.Repository;

import com.rasel.security_demo.model.Cart;
import com.rasel.security_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
