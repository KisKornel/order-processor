package hu.uni_miskolc.order_processor.controllers;

import hu.uni_miskolc.order_processor.dtos.OrderRequest;
import hu.uni_miskolc.order_processor.dtos.OrderResponse;
import hu.uni_miskolc.order_processor.constants.OrderStatus;
import hu.uni_miskolc.order_processor.dtos.UpdateOrderStatusRequest;
import hu.uni_miskolc.order_processor.entities.Order;
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
            if (req.getPaymentType() == null) {
                return ResponseEntity.badRequest().body(new OrderResponse(null, OrderStatus.CANCELED, "Missing paymentMethod"));
            }
            Order order = orderService.placeOrder(req);
            return  ResponseEntity.ok(new OrderResponse(order.getId(), order.getStatus(), "Order placed"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new OrderResponse(null, OrderStatus.CANCELED, ex.getMessage()));
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

    @PutMapping("/update")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @RequestBody UpdateOrderStatusRequest request) {
        if (request == null || request.getOrderId().isBlank() || request.getStatus().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Order order = orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
        return ResponseEntity.ok(new OrderResponse(order.getId(), order.getStatus(), "Order status changed!"));
    }
}