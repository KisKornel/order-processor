package hu.uni_miskolc.order_processor.payments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    CREDIT_CARD,
    PAYPAL,
    INVOICE;

    @JsonCreator
    public static PaymentMethod fromString(String key) {
        if (key == null) return null;
        String normalized = key.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase();
        return PaymentMethod.valueOf(normalized);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
