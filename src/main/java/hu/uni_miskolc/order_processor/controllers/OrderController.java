package hu.uni_miskolc.order_processor.controllers;

import hu.uni_miskolc.order_processor.dtos.OrderRequest;
import hu.uni_miskolc.order_processor.dtos.OrderResponse;
import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;
import hu.uni_miskolc.order_processor.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest req) {
        try {
            if (req.getPaymentMethod() == null) {
                return ResponseEntity.badRequest().body(new OrderResponse(null, "ERROR", "Missing paymentMethod"));
            }
            Order order = orderService.placeOrder(req);
            return  ResponseEntity.ok(new OrderResponse(order.getId(), "OK", "Order placed"));
        } catch (ValidationException | IllegalArgumentException ve) {
            return ResponseEntity.badRequest().body(new OrderResponse(null, "VALIDATION_FAILED", ve.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new OrderResponse(null, "ERROR", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders()
                .stream()
                .map(order -> new OrderResponse(order.getId(), order.getStatus(), "Order placed"))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new OrderResponse(order.getId(), order.getStatus(), "Order placed"));
    }
}