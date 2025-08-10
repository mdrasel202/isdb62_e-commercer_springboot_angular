package com.rasel.security_demo.dto.Cart_and_Order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyNowRequestDTO {
    @NotNull
    private CartItemRequestDTO cartItemRequestDTO;

    @NotNull
    private CheckoutRequestDTO checkoutRequest;
}
