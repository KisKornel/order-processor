package hu.uni_miskolc.order_processor.payments;

import hu.uni_miskolc.order_processor.dtos.PaymentDetails;
import hu.uni_miskolc.order_processor.entities.Order;
import org.springframework.stereotype.Component;

@Component(PaymentType.CREDIT_CARD)
public class CreditCardPayment implements PaymentMethod {
    @Override
    public PaymentResult pay(Order order, PaymentDetails details) {
        if (details == null || details.getCardNumber() == null) {
            return new PaymentResult(false, null, "Missing card details");
        }

        String tx = "cc-" + System.currentTimeMillis();
        return new PaymentResult(true, tx, "Paid with credit card");
    }
}
