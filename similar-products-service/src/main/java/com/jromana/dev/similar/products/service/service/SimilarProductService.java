package com.jromana.dev.similar.products.service.service;

import java.util.List;

import com.jromana.dev.similar.products.service.dto.ProductDTO;

/**
 * Interfaz del servicio encargado de obtener productos similares a un producto dado.
 *
 */
public interface SimilarProductService {

    /**
     * Obtiene los productos similares al producto identificado por
     * {@code productId}.
     *
     * @param productId identificador del producto origen cuyo similares se
     * desean obtener.
     * @return lista de ProductDTO con los productos similares; puede ser vacía
     * si no hay similares o si la implementación decide regresar un fallback
     * ante errores. Nunca debe devolver null.
     */
    List<ProductDTO> getSimilarProducts(String productId);
}
