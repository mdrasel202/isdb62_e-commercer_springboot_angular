package com.rasel.security_demo.dto.Cart_and_Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ShippingAddressDTO {
    private String doorNo;
    private String street;
    private String city;
    private String district;
    private String state;
}
