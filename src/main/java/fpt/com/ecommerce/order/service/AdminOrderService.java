package fpt.com.ecommerce.order.service;

import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.order.dto.response.OrderResponse;

import java.util.List;

public interface AdminOrderService {

    List<OrderResponse> getAll();

    void updateStatus(Long orderId, OrderStatus status);
}
