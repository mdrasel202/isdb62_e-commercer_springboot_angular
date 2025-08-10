package com.rasel.security_demo.dto.Cart_and_Order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CartItemDetailDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer quantity;
    private BigDecimal itemTotal;
}
