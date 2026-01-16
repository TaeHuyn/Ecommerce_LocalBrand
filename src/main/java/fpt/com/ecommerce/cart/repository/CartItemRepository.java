package fpt.com.ecommerce.cart.repository;

import fpt.com.ecommerce.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
