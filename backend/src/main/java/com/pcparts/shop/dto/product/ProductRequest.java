package com.pcparts.shop.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must be at most 150 characters")
        String name,
        @NotBlank(message = "SKU is required")
        @Size(max = 100, message = "SKU must be at most 100 characters")
        String sku,
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive")
        BigDecimal price,
        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,
        @NotBlank(message = "Image URL is required")
        @Size(max = 500, message = "Image URL must be at most 500 characters")
        String imageUrl,
        @Min(value = 0, message = "Stock must be 0 or greater")
        Integer stock
) {
}


