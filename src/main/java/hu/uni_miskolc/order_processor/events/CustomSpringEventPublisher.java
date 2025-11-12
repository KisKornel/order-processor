package hu.uni_miskolc.order_processor.events;

import hu.uni_miskolc.order_processor.entities.Order;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomSpringEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishOrderEvent(final Order order, final String transactionId, final String status) {
        System.out.println("Publishing custom event.");
        GenericSpringEvent<OrderPlacedRecord> customSpringEvent = new GenericSpringEvent<>(this, new OrderPlacedRecord(order, transactionId), status);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }
}
