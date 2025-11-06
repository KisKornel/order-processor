package hu.uni_miskolc.order_processor.payments;

import hu.uni_miskolc.order_processor.dtos.PaymentDetails;
import hu.uni_miskolc.order_processor.entities.Order;
import org.springframework.stereotype.Component;

@Component("INVOICE")
public class InvoicePayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(Order order, PaymentDetails details) {
        String tx = "inv-" + System.currentTimeMillis();
        return new PaymentResult(true, tx, "Invoice created");
    }


}
