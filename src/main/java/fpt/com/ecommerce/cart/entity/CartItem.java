package fpt.com.ecommerce.cart.entity;

import fpt.com.ecommerce.catalog.entity.ProductVariant;
import fpt.com.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"cart_id", "product_variant_id"}
        )
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;
}
