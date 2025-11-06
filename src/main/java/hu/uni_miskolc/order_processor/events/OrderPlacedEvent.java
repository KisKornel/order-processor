package hu.uni_miskolc.order_processor.events;

import hu.uni_miskolc.order_processor.entities.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {
    private final Order order;
    private final String transactionId;

    public OrderPlacedEvent(Object source, Order order, String transactionId) {
        super(source);
        this.order = order;
        this.transactionId = transactionId;
    }
}

