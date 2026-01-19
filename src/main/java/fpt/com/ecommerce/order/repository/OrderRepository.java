package fpt.com.ecommerce.order.repository;

import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.order.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> getByOrOrderCode(String orderCode);

    List<Order> findByOrderStatusAndExpiresAtBefore(OrderStatus orderStatus, Instant expiresAt);
}
