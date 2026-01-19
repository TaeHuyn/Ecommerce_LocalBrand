package fpt.com.ecommerce.order.dto.request;

import fpt.com.ecommerce.common.enums.PaymentMethod;
import lombok.Getter;

@Getter
public class CheckoutRequest {

    private String customerName;
    private String phone;
    private String address;
    private String email;
    private PaymentMethod paymentMethod;
}
