package fpt.com.ecommerce.order.service;

import fpt.com.ecommerce.order.entity.Order;

public interface EmailService {
    void sendOrderConfirmation(String to, Order order);
}
