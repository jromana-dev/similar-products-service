package com.jromana.dev.similar.products.service.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Clase de configuración de la aplicación que expone beans compartidos para
 * ser inyectados en otros componentes de Spring.
 *
 * En particular expone un RestTemplate configurado con un cliente Apache
 * HTTP (CloseableHttpClient) que utiliza un pool de conexiones y timeouts razonables
 * para evitar bloqueos en llamadas a servicios remotos.
 *
 */
@Configuration
public class AppConfig {

    @Value("${similar-products.http.connect-timeout}")
    private int connectTimeoutMs;

    @Value("${similar-products.http.response-timeout}")
    private int responseTimeoutMs;

    @Value("${similar-products.http.max-total-connections}")
    private int maxTotalConnections;

    @Value("${similar-products.http.max-per-route}")
    private int maxPerRoute;

    /**
     * Bean RestTemplate con un cliente Apache HTTP subyacente.
     *
     * Configuración principal:
     * - PoolingHttpClientConnectionManager: controla el tamaño total del pool y por ruta.
     * - RequestConfig: controla tiempos de espera (en segundos) para obtener conexión del pool
     *   y para recibir la respuesta.
     * - HttpComponentsClientHttpRequestFactory: integra el CloseableHttpClient con RestTemplate.
     *
     * @return instancia singleton de RestTemplate para inyección por Spring.
     */
    @Bean
    public RestTemplate restTemplate() {
        // Pool de conexiones para reutilización y mejor rendimiento en llamadas concurrentes.
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxTotalConnections);          // máximo total de conexiones en el pool
        connManager.setDefaultMaxPerRoute(maxPerRoute); // máximo por ruta/host

        // Timeouts modernos (expresados en segundos)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectTimeoutMs))  // tiempo para obtener conexión del pool - 2s
                .setResponseTimeout(Timeout.ofMilliseconds(responseTimeoutMs))           // tiempo máximo para recibir respuesta - 5s
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        // Factoría que permite a RestTemplate usar el CloseableHttpClient configurado.
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(factory);
    }
}
