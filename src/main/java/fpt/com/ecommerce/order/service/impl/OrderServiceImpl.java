package fpt.com.ecommerce.order.service.impl;

import fpt.com.ecommerce.cart.entity.Cart;
import fpt.com.ecommerce.cart.entity.CartItem;
import fpt.com.ecommerce.cart.repository.CartRepository;
import fpt.com.ecommerce.cart.service.CartService;
import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.inventory.service.InventoryService;
import fpt.com.ecommerce.order.dto.request.CheckoutRequest;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.order.entity.OrderItem;
import fpt.com.ecommerce.order.mapper.OrderMapper;
import fpt.com.ecommerce.order.repository.OrderRepository;
import fpt.com.ecommerce.order.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class OrderServiceImpl implements OrderService {

    OrderMapper orderMapper;
    CartService cartService;
    EmailServiceImpl emailService;
    InventoryService inventoryService;
    CartRepository cartRepository;
    OrderRepository orderRepository;

    @Override
    public OrderResponse checkout(String cartToken, CheckoutRequest checkoutRequest) {

        Cart cart = cartRepository.findByCartToken(cartToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("No items in your cart");
        }

        // reserve stock
        for (CartItem item: cart.getItems()) {
            inventoryService.reserve(
                    item.getProductVariant().getId(),
                    item.getQuantity()
            );
        }

        Order order = Order.builder()
                .orderCode(UUID.randomUUID().toString())
                .customerName(checkoutRequest.getCustomerName())
                .phone(checkoutRequest.getPhone())
                .address(checkoutRequest.getAddress())
                .paymentMethod(checkoutRequest.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING)
                .totalAmount(
                        cart.getItems().stream()
                                .mapToInt(i -> i.getPrice() * i.getQuantity())
                                .sum()
                )
                .build();

        order.setOrderItems(
                cart.getItems().stream().map(cartItem ->
                        OrderItem.builder()
                                .order(order)
                                .productVariant(cartItem.getProductVariant())
                                .quantity(cartItem.getQuantity())
                                .price(cartItem.getPrice())
                                .build()
                ).toList()
        );

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cartToken);

        emailService.sendOrderConfirmation(
                checkoutRequest.getEmail(),
                savedOrder.getOrderCode()
        );

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getByOrderCode(String orderCode) {

        Order order = orderRepository.getByOrOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return orderMapper.toResponse(order);
    }
}
