package hu.uni_miskolc.order_processor.events.listeners;

import hu.uni_miskolc.order_processor.events.GenericSpringEvent;
import hu.uni_miskolc.order_processor.events.OrderPlacedRecord;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccountingListener {
    @EventListener(condition = "#event.status == 'COMPLETED'")
    public void onOrderPlacedSuccessful(GenericSpringEvent<OrderPlacedRecord> event) {
        System.out.println("[Accounting | Successful] Record transaction: " + event.getEvent().transactionId());
    }
}
