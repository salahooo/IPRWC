package com.fatbike.shop.dto.order;

import com.fatbike.shop.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        BigDecimal totalPrice,
        OrderStatus status,
        OffsetDateTime createdAt,
        List<OrderItemResponse> items,
        String userEmail,
        String userFullName
) {
}
