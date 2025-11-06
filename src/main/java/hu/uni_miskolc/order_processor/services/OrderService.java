package hu.uni_miskolc.order_processor.services;

import hu.uni_miskolc.order_processor.dtos.OrderRequest;
import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.entities.OrderItem;
import hu.uni_miskolc.order_processor.events.OrderPlacedEvent;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;
import hu.uni_miskolc.order_processor.payments.PaymentMethod;
import hu.uni_miskolc.order_processor.payments.PaymentResult;
import hu.uni_miskolc.order_processor.payments.PaymentStrategy;
import hu.uni_miskolc.order_processor.payments.PaymentStrategyFactory;
import hu.uni_miskolc.order_processor.repositories.OrderRepository;
import hu.uni_miskolc.order_processor.repositories.ProductRepository;
import hu.uni_miskolc.order_processor.validations.FraudValidator;
import hu.uni_miskolc.order_processor.validations.InventoryValidator;
import hu.uni_miskolc.order_processor.validations.LimitValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService implements IOrderService{
    private final ApplicationEventPublisher publisher;
    private final Map<String, PaymentStrategy> paymentStrategies;
    private final InventoryValidator inventoryValidator;
    private final FraudValidator fraudValidator;
    private final LimitValidator limitValidator;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentStrategyFactory paymentFactory;

    @Transactional
    public Order placeOrder(OrderRequest req) throws ValidationException {
        List<OrderItem> items = req.getItems().stream().map(i -> {
            var p = productRepository.findById(i.getProductId()).orElseThrow(() -> new RuntimeException("Product not found: " + i.getProductId()));
            return OrderItem.builder()
                    .productId(i.getProductId())
                    .quantity(i.getQuantity())
                    .price(p.getPrice())
                    .build();
        }).toList();

        PaymentMethod method = req.getPaymentMethod();
        PaymentStrategy strategy = paymentFactory.get(method);

        String orderId = UUID.randomUUID().toString();
        Order order = Order.of(orderId, req.getUserId(), method, items);

        // --- CoR: assemble chain ---
        inventoryValidator.setNext(fraudValidator);
        fraudValidator.setNext(limitValidator);

        // validate (throws ValidationException if invalid)
        inventoryValidator.validate(order);

        // --- Strategy: payment ---
        PaymentResult result = strategy.pay(order, req.getPaymentDetails());
        if (!result.isSuccess()) throw new RuntimeException("Payment failed: " + result.getMessage());

        // --- reserve stock using pessimistic locking inside the same transaction ---
        for (OrderItem item : items) {
            var pOpt = productRepository.findByIdForUpdate(item.getProductId());
            var p = pOpt.orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
            if (p.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + p.getId());
            }
            p.setStock(p.getStock() - item.getQuantity());
            productRepository.save(p);
        }

        // save order
        order.setStatus("PLACED");
        orderRepository.save(order);

        // publish event (Observer)
        publisher.publishEvent(new OrderPlacedEvent(this, order, result.getTransactionId()));

        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }
}
