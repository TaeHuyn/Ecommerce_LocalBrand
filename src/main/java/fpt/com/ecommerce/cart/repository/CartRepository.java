package fpt.com.ecommerce.cart.repository;

import fpt.com.ecommerce.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = "items")
    Optional<Cart> findByCartToken(String cartToken);
}
