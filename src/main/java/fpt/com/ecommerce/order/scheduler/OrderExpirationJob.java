package fpt.com.ecommerce.order.scheduler;

import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.inventory.service.InventoryService;
import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderExpirationJob {

    OrderRepository orderRepository;
    InventoryService inventoryService;

    @Scheduled(fixedRate = 60000) // Chạy mỗi 60 giây
    @Transactional
    public void releaseExpiredOrders() {

        List<Order> expiredOrders = orderRepository.findByOrderStatusAndExpiresAtBefore(
                OrderStatus.PENDING,
                Instant.now()
        );

        for (Order order: expiredOrders) {

            order.getOrderItems().forEach(item -> {
                inventoryService.release(
                        item.getProductVariant().getId(),
                        item.getQuantity()
                );
            });

            order.setOrderStatus(OrderStatus.CANCELLED);
        }
    }
}
