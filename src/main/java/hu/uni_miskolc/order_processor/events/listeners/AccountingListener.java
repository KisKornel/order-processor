package hu.uni_miskolc.order_processor.events.listeners;

import hu.uni_miskolc.order_processor.events.OrderPlacedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccountingListener {
    @EventListener
    public void onOrderPlaced(OrderPlacedEvent evt) {
        System.out.println("[Accounting] Record transaction: " + evt.getTransactionId());
    }
}
