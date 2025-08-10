package com.rasel.security_demo.dto.Cart_and_Order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CartResponseDTO  {
    private Long cartId;
    private Long userId;
    private List<CartItemDetailDTO> items;
    private BigDecimal subtotal;
    private BigDecimal total;
    private BigDecimal discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
