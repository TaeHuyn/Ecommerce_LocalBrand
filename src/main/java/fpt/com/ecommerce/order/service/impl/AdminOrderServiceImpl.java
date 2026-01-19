package fpt.com.ecommerce.order.service.impl;

import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.order.mapper.OrderMapper;
import fpt.com.ecommerce.order.repository.OrderRepository;
import fpt.com.ecommerce.order.service.AdminOrderService;
import lombok.AccessLevel;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderServiceImpl implements AdminOrderService {

    OrderMapper orderMapper;
    OrderRepository orderRepository;

    @Override
    public List<OrderResponse> getAll() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setOrderStatus(status);
    }
}
