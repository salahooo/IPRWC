package com.pcparts.shop.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty(message = "Order items are required")
        @Valid
        List<OrderItemRequest> items
) {
}


