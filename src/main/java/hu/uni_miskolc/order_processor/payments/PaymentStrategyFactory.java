package hu.uni_miskolc.order_processor.payments;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class PaymentStrategyFactory {
    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategyBeans) {
        Map<PaymentMethod, PaymentStrategy> tmp = new EnumMap<>(PaymentMethod.class);
        for (var entry : strategyBeans.entrySet()) {
            try {
                PaymentMethod pm = PaymentMethod.fromString(entry.getKey());
                tmp.put(pm, entry.getValue());
            } catch (IllegalArgumentException e) {
                LoggerFactory.getLogger(getClass()).warn("No PaymentStrategy for {}", entry.getKey());
            }
        }
        this.strategies = Map.copyOf(tmp);
    }

    public PaymentStrategy get(PaymentMethod method) {
        PaymentStrategy s = strategies.get(method);
        if (s == null) throw new IllegalArgumentException("No PaymentStrategy for " + method);
        return s;
    }

    public Set<PaymentMethod> supportedMethods() {
        return strategies.keySet();
    }
}
