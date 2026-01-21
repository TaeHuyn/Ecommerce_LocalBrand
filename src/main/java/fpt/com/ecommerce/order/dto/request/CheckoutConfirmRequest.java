package fpt.com.ecommerce.order.dto.request;

import fpt.com.ecommerce.common.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutConfirmRequest {

    private List<String> reservationKeys;

    private String customerName;
    private String phone;
    private String address;
    private PaymentMethod paymentMethod;
    private String email;

    private Integer totalAmount;
}
