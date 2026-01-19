package fpt.com.ecommerce.order.dto.response;

import fpt.com.ecommerce.common.enums.OrderStatus;
import fpt.com.ecommerce.common.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {

    private Long id;
    private String orderCode;

    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;

    private String customerName;
    private String phone;
    private String address;

    private Integer totalAmount;

    private Instant createdAt;

    private String trackingLink;

    private List<OrderItemResponse> orderItems;
}
