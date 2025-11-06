package hu.uni_miskolc.order_processor.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    private String cardNumber;
    private String expiry;
    private String cvv;
    private String paypalId;
}

