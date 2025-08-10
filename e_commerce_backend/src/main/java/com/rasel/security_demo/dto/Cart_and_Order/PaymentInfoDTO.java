package com.rasel.security_demo.dto.Cart_and_Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoDTO {
    private String method;
    private String paymentStatus;
    private String transactionId;
}
