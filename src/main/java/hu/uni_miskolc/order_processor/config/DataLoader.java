package hu.uni_miskolc.order_processor.config;

import hu.uni_miskolc.order_processor.entities.Product;
import hu.uni_miskolc.order_processor.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        productRepository.save(Product.builder().id("p1").name("Kávéfőző").price(99.99).stock(10).build());
        productRepository.save(Product.builder().id("p2").name("Fejhallgató").price(49.90).stock(20).build());
        productRepository.save(Product.builder().id("p3").name("Mikró").price(149.00).stock(5).build());
        System.out.println("[DataLoader] Inventory seeded (p1,p2,p3)");
    }
}
