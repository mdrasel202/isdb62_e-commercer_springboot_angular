package com.rasel.security_demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "d_products")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Size(min = 5, max = 500)
    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal price;

    @NotNull
    private Integer quantity;

    @NotBlank
    private String category;

    @Column(unique = true)
    private String sku;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductDiscount> discounts = new ArrayList<>();

    public void addImage(ProductImage image){
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image){
        images.remove(image);
        image.setProduct(null);
    }

    public void addDiscount(ProductDiscount discount){
        discounts.add(discount);
        discount.setProduct(this);
    }

    public BigDecimal getCurrentPrice(){
        BigDecimal currentPrice = this.price;
        LocalDateTime now = LocalDateTime.now();
        // Calculate active discounts
        for(ProductDiscount discount : discounts){
            if(discount.isActive() && now.isAfter(discount.getStartDate())&&
                    now.isBefore(discount.getEndDate())){
                BigDecimal discountAmount = currentPrice.multiply(
                        discount.getPercentage().divide(BigDecimal.valueOf(100)));
                currentPrice = currentPrice.subtract(discountAmount);
            }
        }
        return currentPrice.max(BigDecimal.ZERO);
    }

    public void removeDiscount(ProductDiscount discount){
        discounts.remove(discount);
        discount.setProduct(null);
    }
}
