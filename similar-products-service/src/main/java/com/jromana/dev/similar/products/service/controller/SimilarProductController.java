package com.jromana.dev.similar.products.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jromana.dev.similar.products.service.dto.ProductDTO;
import com.jromana.dev.similar.products.service.service.SimilarProductService;

/**
 * Controlador REST que expone el endpoint para obtener los productos similares
 * a un producto dado.
 */
@RestController
@RequestMapping("/product")
public class SimilarProductController {

    private final SimilarProductService similarProductService;

    /**
     * Constructor con inyección del servicio encargado de la lógica de negocio.
     *
     * @param similarProductService servicio que proporciona productos similares
     */
    public SimilarProductController(SimilarProductService similarProductService) {
        this.similarProductService = similarProductService;
    }

    /**
     * Endpoint que obtiene los productos similares para el productId dado.
     *
     * @param productId identificador del producto cuyo similares se desean
     * obtener (extraído de la ruta).
     * @return lista de ProductDTO con los productos similares; puede
     * ser vacía si no hay similares o si el servicio lo devuelve así.
     */
    @GetMapping("/{productId}/similar")
    public List<ProductDTO> getSimilarProducts(@PathVariable String productId) {
        // Delegamos la llamada al servicio que encapsula la lógica/llamadas REST externas.
        return similarProductService.getSimilarProducts(productId);
    }
}
