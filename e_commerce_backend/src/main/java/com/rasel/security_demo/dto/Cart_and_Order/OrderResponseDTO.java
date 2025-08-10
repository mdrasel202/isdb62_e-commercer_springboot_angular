package com.rasel.security_demo.dto.Cart_and_Order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long orderId;
    private String orderNumber;
    private String orderstatus;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
    private ShippingAddressDTO shippingAddress;
    private PaymentInfoDTO paymentInfo;
}
