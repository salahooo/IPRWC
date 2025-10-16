package com.pcparts.shop.controller;

import com.pcparts.shop.dto.order.OrderCreateRequest;
import com.pcparts.shop.dto.order.OrderResponse;
import com.pcparts.shop.dto.order.UpdateOrderStatusRequest;
import com.pcparts.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        // Create a new order tied to the authenticated user and return the persisted state
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> myOrders() {
        // Convenience endpoint for customers to fetch their own history
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        // Guarded in the service so only owners/admins can retrieve the payload
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAll() {
        // Admin overview listing every order irrespective of customer
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        // Allow admins to move an order through its lifecycle
        return ResponseEntity.ok(orderService.updateStatus(id, request));
    }
}


