package fpt.com.ecommerce.order.service.impl;

import fpt.com.ecommerce.cart.entity.Cart;
import fpt.com.ecommerce.cart.repository.CartRepository;
import fpt.com.ecommerce.cart.service.CartService;
import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.order.dto.request.CheckoutConfirmRequest;
import fpt.com.ecommerce.order.dto.response.CheckoutInitResponse;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.order.entity.OrderItem;
import fpt.com.ecommerce.order.mapper.OrderMapper;
import fpt.com.ecommerce.order.repository.OrderRepository;
import fpt.com.ecommerce.order.service.OrderService;
import fpt.com.ecommerce.reservation.entity.Reservation;
import fpt.com.ecommerce.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

        private final OrderMapper orderMapper;
        private final CartRepository cartRepository;
        private final CartService cartService;
        private final ReservationService reservationService;
        private final OrderRepository orderRepository;
        private final EmailServiceImpl emailService;

        @Value("${reservation.ttl.ms:60000}")
        long reservationTtlMs;

        @Override
        public CheckoutInitResponse initCheckout(String cartToken) {

                Cart cart = cartRepository.findByCartToken(cartToken)
                        .orElseThrow(() -> new RuntimeException("Cart not found"));

                if (cart.getItems().isEmpty()) {
                        throw new RuntimeException("Cart is empty");
                }

                List<Reservation> reservations = cart.getItems().stream()
                        .map(item -> reservationService.createReservation(
                                item.getProductVariant(),
                                cart,
                                item.getQuantity(),
                                reservationTtlMs
                        ))
                        .toList();

                int totalAmount = cart.getItems().stream()
                        .mapToInt(i -> i.getPrice() * i.getQuantity())
                        .sum();

                return CheckoutInitResponse.builder()
                        .reservationKeys(
                                reservations.stream()
                                        .map(Reservation::getReservationKey)
                                        .toList()
                        )
                        .expiresAt(Instant.now().plusMillis(reservationTtlMs))
                        .totalAmount(totalAmount)
                        .build();
        }


        @Override
        public OrderResponse confirmCheckout(CheckoutConfirmRequest request) {

                List<Reservation> reservations = request.getReservationKeys().stream()
                        .map(reservationService::getValidReservation)
                        .toList();

                Cart cart = reservations.get(0).getCart();

                Order order = Order.builder()
                        .orderCode(UUID.randomUUID().toString())
                        .customerName(request.getCustomerName())
                        .phone(request.getPhone())
                        .address(request.getAddress())
                        .paymentMethod(request.getPaymentMethod())
                        .orderStatus(OrderStatus.PENDING)
                        .totalAmount(request.getTotalAmount())
                        .build();

                order.setOrderItems(
                        cart.getItems().stream()
                                .map(item -> OrderItem.builder()
                                        .order(order)
                                        .productVariant(item.getProductVariant())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .build())
                                .toList()
                );

                Order savedOrder = orderRepository.save(order);

                for (Reservation r : reservations) {
                        reservationService.commitReservation(r.getReservationKey(), savedOrder);
                }

                cartService.clearCart(cart.getCartToken());

                emailService.sendOrderConfirmation(
                        request.getEmail(),
                        savedOrder
                );

                return orderMapper.toResponse(savedOrder);
        }

        @Override
        public OrderResponse getByOrderCode(String orderCode) {
                Order order = orderRepository.getByOrderCode(orderCode)
                        .orElseThrow(() -> new RuntimeException("Order not found"));
                return orderMapper.toResponse(order);
        }
}
