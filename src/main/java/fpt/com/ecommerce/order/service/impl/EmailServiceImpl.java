package fpt.com.ecommerce.order.service.impl;

import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.order.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;
    TemplateEngine templateEngine;

    @Override
    public void sendOrderConfirmation(
            String toEmail,
            Order order) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Xác nhận đơn hàng #" + order.getOrderCode());

            Context context = new Context();
            context.setVariable("customerName", order.getCustomerName());
            context.setVariable("orderCode", order.getOrderCode());
            context.setVariable("totalAmount", order.getTotalAmount());
            context.setVariable(
                    "trackingLink",
                    "http://localhost:8080/api/orders/" + order.getOrderCode()
            );

            String htmlContent = templateEngine.process(
                    "order-confirmation", context
            );

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
