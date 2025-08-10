package com.rasel.security_demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "d_carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal subtotal;
    private BigDecimal total;
    private BigDecimal discount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    public void addItem(CartItem item){
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item){
        items.add(item);
        item.setCart(null);
    }

    public void calculateTotals() {
        this.subtotal = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;

        for (CartItem item : items) {
            this.subtotal = this.subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            BigDecimal itemDiscount = item.getPrice().subtract(item.getDiscountedPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            this.discount = this.discount.add(itemDiscount);
        }

        this.total = this.subtotal.subtract(this.discount);
    }
}
