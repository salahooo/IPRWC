package com.fatbike.shop.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotNull(message = "Product id is required")
        Long productId,
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,
        @NotNull(message = "Price at purchase is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive")
        BigDecimal priceAtPurchase
) {
}
