package hu.uni_miskolc.order_processor.services;

import hu.uni_miskolc.order_processor.dtos.OrderRequest;
import hu.uni_miskolc.order_processor.entities.Order;
import hu.uni_miskolc.order_processor.exceptions.ValidationException;

import java.util.List;

public interface IOrderService {
    Order placeOrder(OrderRequest req) throws ValidationException;
    List<Order> getAllOrders();
    Order getOrderById(String id);
    Order updateOrderStatus(String orderId, String status);
}
