package hu.uni_miskolc.order_processor.validations;

import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.entities.OrderItem;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;
import hu.uni_miskolc.order_processor.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InventoryValidator extends AbstractValidator<Order> {
    private final ProductRepository productRepository;

    @Override
    public boolean validate(Order order) throws ValidationException {
        for (OrderItem item : order.getItems()) {
            var p = productRepository.findById(item.getProductId());
            if (p.isEmpty() || p.get().getStock() < item.getQuantity()) {
                throw new ValidationException("No stock for product: " + item.getProductId());
            }
        }
        return callNext(order);
    }
}