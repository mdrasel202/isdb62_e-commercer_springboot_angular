package com.rasel.security_demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class DiscountResponseDTO {
    private Long id;
    private String name;
    private BigDecimal percentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private Long productId;
}
