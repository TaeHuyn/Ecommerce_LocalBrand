package fpt.com.ecommerce.order.repository;

import fpt.com.ecommerce.order.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> getByOrderCode(String orderCode);

}
