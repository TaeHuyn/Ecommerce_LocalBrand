package fpt.com.ecommerce.order.service;

import fpt.com.ecommerce.order.dto.request.CheckoutConfirmRequest;
import fpt.com.ecommerce.order.dto.response.CheckoutInitResponse;
import fpt.com.ecommerce.order.dto.response.OrderResponse;

public interface OrderService {

    CheckoutInitResponse initCheckout(String cartToken);

    OrderResponse confirmCheckout(CheckoutConfirmRequest request);

    OrderResponse getByOrderCode(String orderCode);
}
