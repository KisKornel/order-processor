package hu.uni_miskolc.order_processor.dtos;

import hu.uni_miskolc.order_processor.payments.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String userId;
    private List<OrderItemDto> items;
    private PaymentMethod paymentMethod;
    private PaymentDetails paymentDetails;
}