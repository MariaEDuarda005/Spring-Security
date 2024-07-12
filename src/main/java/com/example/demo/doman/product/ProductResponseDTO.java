package com.example.demo.doman.product;

import java.util.UUID;

public record ProductResponseDTO(Long id, String name, Integer price) {
    public ProductResponseDTO(Product product){
        this(product.getId(), product.getName(), product.getPrice());
    }
}
