package com.jromana.dev.similar.products.service.service;

import java.util.List;

import com.jromana.dev.similar.products.service.dto.ProductDTO;

public interface SimilarProductService {
    
    List<ProductDTO> getSimilarProducts(String productId);
}
