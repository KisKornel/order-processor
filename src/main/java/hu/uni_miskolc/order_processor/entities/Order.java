package hu.uni_miskolc.order_processor.entities;

import hu.uni_miskolc.order_processor.payments.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private double total;
    private String status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public static Order of(String id, String userId, PaymentMethod paymentMethod, List<OrderItem> items) {
        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        return new Order(id, userId, total, "CREATED", paymentMethod, items);
    }
}
