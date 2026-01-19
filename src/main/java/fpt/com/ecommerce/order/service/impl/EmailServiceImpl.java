package fpt.com.ecommerce.order.service.impl;

import fpt.com.ecommerce.order.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmation(String to, String orderCode) {

        String trackingLink = "http://localhost:8080/api/orders/" + orderCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Order Confirmation - " + orderCode);
        message.setText(
                """
                        Thank you for your order!
                        
                        You can track your order here:
                        %s
                        """.formatted(trackingLink)
        );

        mailSender.send(message);
    }
}
