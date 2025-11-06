package hu.uni_miskolc.order_processor.repositories;

import hu.uni_miskolc.order_processor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
