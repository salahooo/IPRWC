package com.pcparts.shop.controller;

import com.pcparts.shop.dto.product.ProductRequest;
import com.pcparts.shop.dto.product.ProductResponse;
import com.pcparts.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        // Public catalog listing consumed by the storefront
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        // Fetch a single product or surface a 404 when missing
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        // Admin-only endpoint to expand the catalog
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        // Maintain a productâ€™s details while keeping SKU uniqueness intact
        ProductResponse response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Remove a product from the catalog when itâ€™s no longer offered
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


