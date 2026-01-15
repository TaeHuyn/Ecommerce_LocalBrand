package fpt.com.ecommerce.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_variants",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product_id", "size", "color"}
        )
       )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private String color;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
