package hu.uni_miskolc.order_processor.events.listeners;

import hu.uni_miskolc.order_processor.events.OrderPlacedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @EventListener
    @Async
    public void onOrderPlaced(OrderPlacedEvent evt) {
        System.out.println("[Notification] Order " + evt.getOrder().getId() + " placed. tx=" + evt.getTransactionId());
    }
}
