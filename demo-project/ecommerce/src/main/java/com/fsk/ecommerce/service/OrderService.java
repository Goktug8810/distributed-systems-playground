package com.fsk.ecommerce.service;

import com.fsk.ecommerce.entity.Order;
import com.fsk.ecommerce.entity.OrderItem;
import com.fsk.ecommerce.entity.OrderStatus;
import com.fsk.ecommerce.entity.Product;
import com.fsk.ecommerce.entity.User;
import com.fsk.ecommerce.mapper.dto.OrderRequestDTO;
import com.fsk.ecommerce.repository.OrderItemRepository;
import com.fsk.ecommerce.repository.OrderRepository;
import com.fsk.ecommerce.repository.ProductRepository;
import com.fsk.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public UUID createOrder(OrderRequestDTO request, String mode) {
        if ("fixed".equalsIgnoreCase(mode)) {
            return createOrderWithLock(request);
        } else {
            return createOrderNaive(request);
        }
    }

    @Transactional
    public UUID createOrderNaive(OrderRequestDTO request) {
        // Naive implementation prone to Race Condition (Problem 1.a)
        
        UUID userId = request.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        
        // Save order first to get ID
        Order savedOrder = orderRepository.save(order);
        
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderRequestDTO.OrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // CHECK STOCK
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Quantity not available for product: " + product.getName());
            }

            // ARTIFICIAL DELAY to increase chance of race condition
            try {
                Thread.sleep(50); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // UPDATE STOCK
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder); // Set reference to saved order
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            
            // Calculate item price
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(itemTotal);
            
            // Save OrderItem explicitly
            orderItemRepository.save(orderItem);
            
            totalPrice = totalPrice.add(itemTotal);
        }

        // Update total amount on order
        savedOrder.setTotalAmount(totalPrice);
        orderRepository.save(savedOrder);
        
        log.info("Order created: {}", savedOrder.getId());
        return savedOrder.getId();
    }

    @Transactional
    public UUID createOrderWithLock(OrderRequestDTO request) {
        throw new UnsupportedOperationException("Fixed implementation not yet available");
    }
}
