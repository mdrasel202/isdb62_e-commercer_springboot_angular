package com.rasel.security_demo.model;

import com.rasel.security_demo.constants.OrderStatus;
import com.rasel.security_demo.model.embeddable.PaymentInfo;
import com.rasel.security_demo.model.embeddable.ShippingAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "d_orders")
public class Order  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> item = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private String notes;

    @Embedded
    private ShippingAddress shippingAddress;

    @Embedded
    private PaymentInfo paymentInfo;

    @CreationTimestamp
    private LocalDateTime orderDate;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
