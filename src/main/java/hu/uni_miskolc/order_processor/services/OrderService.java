package hu.uni_miskolc.order_processor.services;

import hu.uni_miskolc.order_processor.dtos.OrderRequest;
import hu.uni_miskolc.order_processor.constants.OrderStatus;
import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.entities.OrderItem;
import hu.uni_miskolc.order_processor.entities.Product;
import hu.uni_miskolc.order_processor.events.CustomSpringEventPublisher;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;
import hu.uni_miskolc.order_processor.payments.PaymentMethod;
import hu.uni_miskolc.order_processor.payments.PaymentResult;
import hu.uni_miskolc.order_processor.payments.PaymentMethodFactory;
import hu.uni_miskolc.order_processor.repositories.OrderRepository;
import hu.uni_miskolc.order_processor.repositories.ProductRepository;
import hu.uni_miskolc.order_processor.validations.FraudValidator;
import hu.uni_miskolc.order_processor.validations.InventoryValidator;
import hu.uni_miskolc.order_processor.validations.LimitValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService implements IOrderService{
    private final CustomSpringEventPublisher customSpringEventPublisher;
    private final InventoryValidator inventoryValidator;
    private final FraudValidator fraudValidator;
    private final LimitValidator limitValidator;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentMethodFactory paymentMethodFactory;

    @Override
    @Transactional
    public Order placeOrder(OrderRequest req) throws ValidationException {
        List<OrderItem> items = req.getItems().stream().map(item -> {
            var p = productRepository.findById(item.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found with " + item.getProductId() + " id"));
            return OrderItem.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .price(p.getPrice())
                    .build();
        }).toList();

        String orderId = UUID.randomUUID().toString();
        Order order = Order.of(orderId, req.getUserId(), OrderStatus.CREATED, req.getPaymentType(), items);

        // --- CoR ---
        inventoryValidator.setNext(fraudValidator);
        fraudValidator.setNext(limitValidator);
        inventoryValidator.validate(order);

        // --- Strategy: payment ---
        PaymentMethod strategy = paymentMethodFactory.get(req.getPaymentType());
        PaymentResult result = strategy.pay(order, req.getPaymentDetails());

        if (!result.isSuccess()) throw new RuntimeException("Payment failed: " + result.getMessage());

        for (OrderItem item : items) {
            Product product = productRepository.findByIdForUpdate(item.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found with " + item.getProductId() + " id"));
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + product.getId());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setTransactionId(result.getTransactionId());
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        // publish event (Observer)
        customSpringEventPublisher.publishOrderEvent(order, result.getTransactionId(), OrderStatus.PROCESSING);

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

    @Override
    @Transactional
    public Order updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(newStatus);

        customSpringEventPublisher.publishOrderEvent(order, order.getTransactionId(), newStatus);
        return order;
    }
}
