package fpt.com.ecommerce.cart.entity;

import fpt.com.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cartToken;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<CartItem> items = new ArrayList<>();
}
