package com.pcparts.shop.service;

import com.pcparts.shop.dto.order.OrderCreateRequest;
import com.pcparts.shop.dto.order.OrderItemRequest;
import com.pcparts.shop.dto.order.OrderResponse;
import com.pcparts.shop.dto.order.UpdateOrderStatusRequest;
import com.pcparts.shop.entity.Order;
import com.pcparts.shop.entity.OrderItem;
import com.pcparts.shop.entity.OrderStatus;
import com.pcparts.shop.entity.Product;
import com.pcparts.shop.entity.User;
import com.pcparts.shop.exception.BadRequestException;
import com.pcparts.shop.exception.ResourceNotFoundException;
import com.pcparts.shop.mapper.OrderMapper;
import com.pcparts.shop.repository.OrderRepository;
import com.pcparts.shop.repository.ProductRepository;
import com.pcparts.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderResponse createOrder(OrderCreateRequest request) {
        User user = getCurrentUser();
        if (request.items().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        // Build the aggregate with the authenticated user and default status
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        // Translate incoming item DTOs into persistent order items tied to the same order
        List<OrderItem> orderItems = request.items().stream()
                .map(itemRequest -> createOrderItem(order, itemRequest))
                .collect(Collectors.toList());
        order.setItems(orderItems);

        // Snapshot the total price at purchase-time so future price changes do not affect history
        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        Order saved = orderRepository.save(order);
        return OrderMapper.toDto(saved);
    }

    public List<OrderResponse> getMyOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        // Only the owner or an admin should be able to inspect a given order
        verifyAccess(order);
        return OrderMapper.toDto(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        // Admin-triggered status transitions are the only mutable part of an order
        order.setStatus(request.status());
        Order saved = orderRepository.save(order);
        return OrderMapper.toDto(saved);
    }

    private OrderItem createOrderItem(Order order, OrderItemRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.productId()));
        // Capture the product's price at this moment to avoid later price drift
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setPriceAtPurchase(product.getPrice());
        return item;
    }

    private void verifyAccess(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        // Owners can see their own orders, administrators can view all
        if (!isAdmin && !order.getUser().getUsername().equals(authentication.getName())) {
            throw new AccessDeniedException("You are not authorized to view this order");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Resolve the security principal to our persisted user entity
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authentication.getName()));
    }
}


