package hu.uni_miskolc.order_processor.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class PaymentStrategyFactory {
    private static final Logger log = LoggerFactory.getLogger(PaymentStrategyFactory.class);

    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategyBeans) {
        EnumMap<PaymentMethod, PaymentStrategy> tmp = new EnumMap<>(PaymentMethod.class);

        for (var entry : strategyBeans.entrySet()) {
            String beanName = entry.getKey();
            PaymentStrategy bean = entry.getValue();

            try {
                PaymentMethod pm = PaymentMethod.fromString(beanName);
                if (tmp.containsKey(pm)) {
                    throw new IllegalStateException("Duplicate PaymentStrategy for " + pm +
                            " (bean '" + beanName + "' vs existing '" + tmp.get(pm).getClass().getName() + "')");
                }
                tmp.put(pm, bean);
                log.info("Registered payment strategy for {} -> bean='{}' ({})", pm, beanName, bean.getClass().getName());
            } catch (IllegalArgumentException e) {
                log.warn("Skipping bean '{}' : cannot map bean name to PaymentMethod", beanName);
            }
        }

        this.strategies = Map.copyOf(tmp);
    }

    public PaymentStrategy get(PaymentMethod method) {
        var s = strategies.get(method);
        if (s == null) throw new IllegalArgumentException("No PaymentStrategy for " + method);
        return s;
    }
}
