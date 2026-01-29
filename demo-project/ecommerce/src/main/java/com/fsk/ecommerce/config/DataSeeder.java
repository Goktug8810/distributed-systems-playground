package com.fsk.ecommerce.config;

import com.fsk.ecommerce.entity.Product;
import com.fsk.ecommerce.entity.User;
import com.fsk.ecommerce.repository.ProductRepository;
import com.fsk.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public static final UUID PRODUCT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    // Using a valid UUID for User
    public static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Override
    public void run(String... args) throws Exception {
        // Since IDs are generated, we check by property (e.g. SKU) or just create if empty?
        // Let's check by SKU to be safe.
        if (!productRepository.existsBySku("abc-123")) {
            Product product = new Product();
            // product.setId(PRODUCT_ID); // Generated
            product.setName("Test Product");
            product.setSku("abc-123");
            product.setStockQuantity(100);
            product.setPrice(new BigDecimal("100.00"));
            product.setCategory("Electronics");
            
            productRepository.save(product);
            System.out.println("Seeded Product: " + product.getId());
        }

        if (!userRepository.existsByEmail("test@example.com")) {
            User user = new User();
            // user.setId(USER_ID); // Generated
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setFirstName("Test");
            user.setLastName("User");
            // Password not supported in User entity, skipping.
            
            userRepository.save(user);
            System.out.println("Seeded User: " + user.getId());
        }
    }
}
