package fpt.com.ecommerce.order.service;

public interface EmailService {
    void sendOrderConfirmation(String to, String orderCode);
}
