package hu.uni_miskolc.order_processor.payments;

import hu.uni_miskolc.order_processor.dtos.PaymentDetails;
import hu.uni_miskolc.order_processor.entities.Order;

public interface PaymentMethod {
    PaymentResult pay(Order order, PaymentDetails details);
}
