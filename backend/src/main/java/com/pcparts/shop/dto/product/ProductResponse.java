package com.pcparts.shop.dto.product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        String description,
        String imageUrl,
        Integer stock,
        OffsetDateTime createdAt
) {
}


