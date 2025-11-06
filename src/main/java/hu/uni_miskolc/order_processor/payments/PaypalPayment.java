package hu.uni_miskolc.order_processor.payments;

import hu.uni_miskolc.order_processor.dtos.PaymentDetails;
import hu.uni_miskolc.order_processor.entities.Order;
import org.springframework.stereotype.Component;

@Component("PAYPAL")
public class PaypalPayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(Order order, PaymentDetails details) {
        if (details == null || details.getPaypalId() == null) {
            return new PaymentResult(false, null, "Missing PayPal id");
        }
        String tx = "pp-" + System.currentTimeMillis();
        return new PaymentResult(true, tx, "Paid with PayPal");
    }
}
