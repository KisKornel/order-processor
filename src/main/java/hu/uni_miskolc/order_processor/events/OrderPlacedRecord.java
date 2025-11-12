package hu.uni_miskolc.order_processor.events;

import hu.uni_miskolc.order_processor.entities.Order;

public record OrderPlacedRecord(Order order, String transactionId) {
}