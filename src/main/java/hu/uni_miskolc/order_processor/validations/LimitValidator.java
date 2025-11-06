package hu.uni_miskolc.order_processor.validations;

import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LimitValidator extends AbstractValidator<Order> {

    @Override
    public boolean validate(Order order) throws ValidationException {
        int LIMIT = 5;
        if (order.getItems().size() > LIMIT) {
                throw new ValidationException("Product limit max 5");
        }

        return callNext(order);
    }
}
