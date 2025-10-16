package com.pcparts.shop.mapper;

import com.pcparts.shop.dto.order.OrderItemResponse;
import com.pcparts.shop.dto.order.OrderResponse;
import com.pcparts.shop.entity.Order;
import com.pcparts.shop.entity.OrderItem;

import java.util.List;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toDto(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderMapper::mapItem)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                order.getUser().getEmail(),
                order.getUser().getFullName()
        );
    }

    private static OrderItemResponse mapItem(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getSku(),
                item.getQuantity(),
                item.getPriceAtPurchase(),
                item.getProduct().getImageUrl()
        );
    }
}


