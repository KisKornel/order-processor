package hu.uni_miskolc.order_processor.events.listeners;

import hu.uni_miskolc.order_processor.events.GenericSpringEvent;
import hu.uni_miskolc.order_processor.events.OrderPlacedRecord;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @EventListener(condition = "#event.status == 'PROCESSING'")
    @Async
    public void onOrderPlacedProcessing(GenericSpringEvent<OrderPlacedRecord> event) {
        System.out.println("[Notification | Processing] Order is processing with " + event.getEvent().order().getId() + " id. tx=" + event.getEvent().transactionId());
    }

    @EventListener(condition = "#event.status == 'SHIPPING'")
    @Async
    public void onOrderPlacedShipping(GenericSpringEvent<OrderPlacedRecord> event) {
        System.out.println("[Notification | Shipping] Prepare shipment for order " + event.getEvent().order().getId() + ". tx=" + event.getEvent().transactionId());
    }

    @EventListener(condition = "#event.status == 'COMPLETED'")
    @Async
    public void onOrderPlacedCompleted(GenericSpringEvent<OrderPlacedRecord> event) {
        System.out.println("[Notification | Completed] Order completed with " + event.getEvent().order().getId() + " id. tx=" + event.getEvent().transactionId());
    }

    @EventListener(condition = "#event.status == 'CANCELED'")
    @Async
    public void onOrderPlacedCanceled(GenericSpringEvent<OrderPlacedRecord> event) {
        System.out.println("[Notification | Canceled] Order canceled with " + event.getEvent().order().getId() + " id. tx=" + event.getEvent().transactionId());
    }
}
