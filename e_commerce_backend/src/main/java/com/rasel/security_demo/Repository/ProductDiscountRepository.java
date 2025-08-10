package com.rasel.security_demo.Repository;

import com.rasel.security_demo.model.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    List<ProductDiscount> findByProductId(Long productId);
    List<ProductDiscount> findByActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endDate);
}
