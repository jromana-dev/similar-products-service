package com.jromana.dev.similar.products.service.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jromana.dev.similar.products.service.dto.ProductDTO;

/**
 * Servicio que obtiene la lista de productos similares a partir de un servicio mock externo. Esta implementación realiza dos pasos:
 *  1. Consulta los IDs de productos similares para un productId dado.
 *  2. Por cada ID devuelve el detalle del producto llamando al endpoint de detalle.
 *
 * Notas sobre el comportamiento:
 * - Si la llamada de IDs falla, se devuelve una lista vacía como fallback.
 * - Si la llamada de detalle para un ID concreto falla, se omite ese producto y se continúa con el resto.
 * 
 */
@Service
public class SimilarProductServiceImpl implements SimilarProductService {

    /**
     * Logger para registrar información y errores ocurridos durante las
     * llamadas a los servicios remotos.
     */
    private static final Logger logger = LoggerFactory.getLogger(SimilarProductServiceImpl.class);

    /**
     * Cliente HTTP utilizado para realizar las llamadas REST al mock. Se
     * inyecta por Spring en el constructor.
     */
    private final RestTemplate restTemplate;

    /**
     * URL base del servicio mock. Se inyecta desde la propiedad
     */
    private final String mockBaseUrl;

    /**
     * Constructor del servicio.
     *
     * @param restTemplate cliente HTTP inyectado por Spring para realizar las
     * peticiones.
     * @param mockBaseUrl URL base del servicio mock, inyectada desde la
     * configuración.
     */
    public SimilarProductServiceImpl(RestTemplate restTemplate,
            @Value("${mock.base.url}") String mockBaseUrl) {
        this.restTemplate = restTemplate;
        this.mockBaseUrl = mockBaseUrl;
    }

    /**
     * Obtiene los productos similares para el identificador de producto dado.
     *
     * @param productId identificador del producto del que se desean obtener
     * similares.
     * @return lista de ProductDTO con los productos similares. Puede
     * ser vacía si no hay similares o si ocurren errores.
     */
    @Override
    public List<ProductDTO> getSimilarProducts(String productId) {
        // Construye la URL para pedir los IDs de productos similares.
        String idsUrl = mockBaseUrl + productId + "/similarids";

        String[] similarIds;

        //  1. Llamada al mock de similar IDs
        try {
            similarIds = restTemplate.getForObject(idsUrl, String[].class);
        } catch (RestClientException e) {
            // Registro del error y retorno seguro: lista vacía como fallback.
            logger.error("Error fetching similar IDs for product {}: {}", productId, e.getMessage());
            return List.of(); // fallback: lista vacía si falla
        }

        // Si no hay IDs, devolvemos lista vacía.
        if (similarIds == null || similarIds.length == 0) {
            logger.info("No similar products found for product {}", productId);
            return List.of();
        }

        // 2. Llamada al mock de detalle para cada ID
        return Arrays.stream(similarIds)
                .map(id -> {
                    String productUrl = mockBaseUrl + id;
                    try {
                        // Intentamos obtener el detalle del producto y mapearlo a ProductDTO.
                        return restTemplate.getForObject(productUrl, ProductDTO.class);
                    } catch (RestClientException e) {
                        // Si falla una petición de detalle concreta, la ignoramos y seguimos con las demás.
                        logger.warn("Product ID {} could not be fetched, skipping.", id);
                        return null; // ignoramos este producto y seguimos
                    }
                })
                .filter(p -> p != null) // eliminamos nulos
                .toList();
    }

}
