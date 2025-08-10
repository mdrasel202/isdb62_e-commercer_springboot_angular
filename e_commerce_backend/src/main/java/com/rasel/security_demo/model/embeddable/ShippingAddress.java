package com.rasel.security_demo.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ShippingAddress {
    private String doorNo;
    private String street;
    private String city;
    private String district;
    private String state;
}
