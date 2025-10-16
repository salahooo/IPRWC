package com.pcparts.shop.service;

import com.pcparts.shop.dto.product.ProductRequest;
import com.pcparts.shop.dto.product.ProductResponse;
import com.pcparts.shop.entity.Product;
import com.pcparts.shop.exception.BadRequestException;
import com.pcparts.shop.exception.ResourceNotFoundException;
import com.pcparts.shop.mapper.ProductMapper;
import com.pcparts.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> findAll() {
        // Eagerly map every entity to its DTO to avoid leaking JPA internals to the controller
        return productRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(Long id) {
        // Fail fast when the requested product is missing so the controller can return 404
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductMapper.toDto(product);
    }

    public ProductResponse create(ProductRequest request) {
        // Only allow unique SKUs to keep the catalog searchable by SKU later on
        if (productRepository.existsBySku(request.sku())) {
            throw new BadRequestException("SKU already exists");
        }
        Product product = ProductMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return ProductMapper.toDto(saved);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        // Reuse the 404 guard so updates fail cleanly for phantom IDs
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        // Reject SKU changes that would collide with another existing product
        if (!product.getSku().equalsIgnoreCase(request.sku()) && productRepository.existsBySku(request.sku())) {
            throw new BadRequestException("SKU already exists");
        }
        ProductMapper.updateEntity(product, request);
        Product saved = productRepository.save(product);
        return ProductMapper.toDto(saved);
    }

    public void delete(Long id) {
        // Mirror findById guard so delete follows the same 404 semantics
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}


