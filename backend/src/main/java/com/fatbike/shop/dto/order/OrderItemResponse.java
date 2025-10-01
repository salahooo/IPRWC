package com.fatbike.shop.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        String productSku,
        Integer quantity,
        BigDecimal priceAtPurchase,
        String imageUrl
) {
}
