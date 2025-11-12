package hu.uni_miskolc.order_processor.events.listeners;

import hu.uni_miskolc.order_processor.events.GenericSpringEvent;
import hu.uni_miskolc.order_processor.events.OrderPlacedRecord;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShippingListener {
    @EventListener(condition = "#event.status == 'SHIPPING'")
    public void onOrderPlacedSuccessful(GenericSpringEvent<OrderPlacedRecord> event) {
        System.out.println("[Shipping | Successful] Prepare shipment for order " + event.getEvent().order().getId());
    }
}
