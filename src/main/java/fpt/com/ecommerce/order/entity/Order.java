package fpt.com.ecommerce.order.entity;

import fpt.com.ecommerce.common.entity.BaseEntity;
import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderCode;

    private String customerName;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Integer totalAmount;

    private Instant expiresAt;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItems;
}
