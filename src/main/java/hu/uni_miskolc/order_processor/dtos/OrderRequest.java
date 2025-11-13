package hu.uni_miskolc.order_processor.dtos;

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
    private String paymentType;
    private PaymentDetails paymentDetails;
}