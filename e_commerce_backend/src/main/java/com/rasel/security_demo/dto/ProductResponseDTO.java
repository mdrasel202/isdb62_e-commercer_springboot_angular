package com.rasel.security_demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal currentPrice;
    private Integer quantity;
    private String category;
    private String sku;
    private List<ImageResponseDTO> images;
    private List<DiscountResponseDTO> discounts;
}
