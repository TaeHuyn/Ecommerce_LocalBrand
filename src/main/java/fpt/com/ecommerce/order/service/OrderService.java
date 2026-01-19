package fpt.com.ecommerce.order.service;

import fpt.com.ecommerce.order.dto.request.CheckoutRequest;
import fpt.com.ecommerce.order.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse checkout(String cartToken, CheckoutRequest checkoutRequest);

    OrderResponse getByOrderCode(String orderCode);
}
