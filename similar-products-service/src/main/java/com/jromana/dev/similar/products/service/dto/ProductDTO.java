package com.jromana.dev.similar.products.service.dto;

/**
 * DTO con inmutabilidad para representar un producto.
 */
public record ProductDTO(String id, String name, Double price, boolean availability) {
    
}
