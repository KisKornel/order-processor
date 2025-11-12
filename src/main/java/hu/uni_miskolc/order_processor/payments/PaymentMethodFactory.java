package hu.uni_miskolc.order_processor.payments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PaymentMethodFactory {
    private final Map<String, PaymentMethod> paymentMethods;

    public PaymentMethod get(String paymentType) {
        PaymentMethod paymentMethod = paymentMethods.get(paymentType);
        if (Objects.isNull(paymentMethod)) {
            throw new IllegalArgumentException("Unsupported payment type");
        }
        return paymentMethod;
    }
}
