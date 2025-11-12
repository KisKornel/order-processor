package hu.uni_miskolc.order_processor.config;

import hu.uni_miskolc.order_processor.entities.OrderItem;
import hu.uni_miskolc.order_processor.events.CustomSpringEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class CustomSpringEventRunnerTest implements CommandLineRunner {
    private final CustomSpringEventPublisher customSpringEventPublisher;


    public List<OrderItem> fillItemList() {
        List<OrderItem> itemList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            OrderItem item = OrderItem.builder().price(10.2 + i).quantity(i + 1).productId("prod-" + i).build();
            itemList.add(item);
        }

        return itemList;
    }

    @Override
    public void run(String... args) {
        /*
        List<OrderItem> orderItem = fillItemList();
        Order order1 = Order.builder().id("order-1").items(orderItem).userId("user-1").build();
        Order order2 = Order.builder().id("order-2").items(orderItem).userId("user-1").build();
        Order order3 = Order.builder().id("order-3").items(orderItem).userId("user-1").build();

        customSpringEventPublisher.publishOrderEvent(order1,"transaction-1", true);
        customSpringEventPublisher.publishOrderEvent(order2,"transaction-2", true);
        customSpringEventPublisher.publishOrderEvent(order3, "transaction-3",false);
        */
    }
}
