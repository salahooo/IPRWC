package com.fatbike.shop.mapper;

import com.fatbike.shop.dto.product.ProductRequest;
import com.fatbike.shop.dto.product.ProductResponse;
import com.fatbike.shop.entity.Product;

import java.time.OffsetDateTime;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .sku(request.sku())
                .price(request.price())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .stock(request.stock())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public static void updateEntity(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setSku(request.sku());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setImageUrl(request.imageUrl());
        product.setStock(request.stock());
    }

    public static ProductResponse toDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                product.getDescription(),
                product.getImageUrl(),
                product.getStock(),
                product.getCreatedAt()
        );
    }
}
