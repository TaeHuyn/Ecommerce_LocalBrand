package fpt.com.ecommerce.reservation.entity;

import fpt.com.ecommerce.catalog.entity.ProductVariant;
import fpt.com.ecommerce.cart.entity.Cart;
import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private String status; // HELD, COMMITTED, CANCELLED, EXPIRED

    @Column(nullable = false, unique = true)
    private String reservationKey;
}
