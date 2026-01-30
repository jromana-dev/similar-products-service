package com.jromana.dev.similar.products.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Clase de configuración de la aplicación que expone beans compartidos para
 * ser inyectados en otros componentes de Spring.
 */
@Configuration
public class AppConfig {

    /**
     * Bean RestTemplate utilizado para realizar peticiones REST hacia servicios externos.
     *
     * Al exponerlo como bean se facilita su reutilización y la posibilidad de
     * aplicar configuraciones/colaboradores (interceptors, message converters, timeouts)
     * desde un único punto de la aplicación.
     *
     * @return instancia singleton de RestTemplate para inyección por Spring.
     */
    @Bean
    public RestTemplate restTemplate() {
        // Se devuelve una instancia básica; personalizar aquí si se necesitan timeouts, interceptors, etc.
        return new RestTemplate();
    }
}
