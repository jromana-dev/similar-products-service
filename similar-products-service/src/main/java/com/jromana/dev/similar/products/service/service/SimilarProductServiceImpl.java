package com.jromana.dev.similar.products.service.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jromana.dev.similar.products.service.dto.ProductDTO;

/**
 * Servicio que obtiene la lista de productos similares a partir de un servicio
 * mock externo. Esta implementación realiza dos pasos: 1. Consulta los IDs de
 * productos similares para un productId dado. 2. Por cada ID devuelve el
 * detalle del producto llamando al endpoint de detalle.
 *
 * Notas sobre el comportamiento: - Si la llamada de IDs falla, se devuelve una
 * lista vacía como fallback. - Si la llamada de detalle para un ID concreto
 * falla, se omite ese producto y se continúa con el resto.
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
     * Obtiene una lista de productos similares a partir del ID de un producto.
     * 
     * <p>Este método realiza las siguientes operaciones:
     *  1. Construye una URL para obtener los IDs de productos similares desde el servicio mock
     *  2. Realiza una solicitud HTTP para obtener los IDs similares
     *  3. Si la solicitud falla o no hay resultados, retorna una lista vacía
     *  4. Obtiene los detalles de cada producto de forma asincrónica y concurrente
     *  5. Espera a que se completen todas las solicitudes asincrónicas
     *  6. Retorna una lista con los productos válidos obtenidos
     *
     * @param productId el identificador del producto del cual se desean obtener productos similares
     * @return una lista de ProductDTO con los productos similares encontrados,
     *         o una lista vacía si no se encuentran productos similares o ocurre un error
     */
    @Override
    public List<ProductDTO> getSimilarProducts(String productId) {
        // Construye la URL para pedir los IDs de productos similares.
        String idsUrl = mockBaseUrl + productId + "/similarids";

        String[] similarIds;

        // 1. Obtener IDs similares
        try {
            similarIds = restTemplate.getForObject(idsUrl, String[].class);
        } catch (RestClientException e) {
            logger.error("Error fetching similar IDs for product {}: {}",
                    productId, e.getMessage());
            return List.of();
        }

        if (similarIds == null || similarIds.length == 0) {
            return List.of();
        }

        // 2. Fetch concurrente
        List<CompletableFuture<ProductDTO>> futures
                = Arrays.stream(similarIds)
                        .map(this::fetchProductAsync)
                        .toList();

        // 3. Esperar a todos los similares
        CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new)
        ).join();

        // Recoger resultados válidos
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Obtiene de forma asincrónica los datos de un producto mediante su identificador.
     *
     * Este método realiza una llamada HTTP GET a un servicio remoto para recuperar
     * la información del producto especificado. La ejecución es asincrónica utilizando
     * el ejecutor configurado 'similarProductsExecutor'.
     *
     * @param productId el identificador único del producto a recuperar
     * @return un CompletableFuture que se completa con un ProductDTO si la obtención es exitosa,
     *         o con null si ocurre un error durante la llamada HTTP
     * @throws RestClientException capturada internamente y registrada como advertencia,
     *                             devolviendo un futuro completado con null
     */
    @Async("similarProductsExecutor")
    public CompletableFuture<ProductDTO> fetchProductAsync(String productId) {

        try {
            String url = mockBaseUrl + productId;
            ProductDTO product
                    = restTemplate.getForObject(url, ProductDTO.class);
            return CompletableFuture.completedFuture(product);

        } catch (RestClientException e) {
            logger.warn("Product ID {} could not be fetched: {}",
                    productId, e.getMessage());
            return CompletableFuture.completedFuture(null);
        }
    }
}
