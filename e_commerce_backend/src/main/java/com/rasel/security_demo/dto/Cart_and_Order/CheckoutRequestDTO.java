package com.rasel.security_demo.dto.Cart_and_Order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDTO {
    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    private String notes;
}
