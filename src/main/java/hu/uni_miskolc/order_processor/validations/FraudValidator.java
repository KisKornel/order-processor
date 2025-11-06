package hu.uni_miskolc.order_processor.validations;

import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class FraudValidator extends AbstractValidator<Order> {
    @Override
    public boolean validate(Order order) throws ValidationException {
        if (order.getTotal() > 10000) {
            throw new ValidationException("Fraud suspected: amount > 10000");
        }
        return callNext(order);
    }
}
