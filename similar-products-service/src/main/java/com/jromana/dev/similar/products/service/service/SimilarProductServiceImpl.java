package com.jromana.dev.similar.products.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jromana.dev.similar.products.service.dto.ProductDTO;

@Service
public class SimilarProductServiceImpl implements SimilarProductService {

    @Override
    public List<ProductDTO> getSimilarProducts(String productId) {
        // Implementación del método para obtener productos similares
        return List.of(); // Retorna una lista vacía como ejemplo
    }
    
}
