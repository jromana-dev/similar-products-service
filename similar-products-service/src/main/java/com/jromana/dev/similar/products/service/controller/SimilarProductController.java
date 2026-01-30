package com.jromana.dev.similar.products.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jromana.dev.similar.products.service.dto.ProductDTO;
import com.jromana.dev.similar.products.service.service.SimilarProductService;

@RestController
@RequestMapping("/product")
public class SimilarProductController {

    private final SimilarProductService similarProductService;

    // Inyección de la interfaz en el constructor
    public SimilarProductController(SimilarProductService similarProductService) {
        this.similarProductService = similarProductService;
    }

    @GetMapping("/{productId}/similar")
    public List<ProductDTO> getSimilarProducts(@PathVariable String productId) {
        return similarProductService.getSimilarProducts(productId);
    }
}
