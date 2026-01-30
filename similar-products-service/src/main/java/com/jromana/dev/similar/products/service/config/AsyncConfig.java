package com.jromana.dev.similar.products.service.config;


import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Value("${similar.products.thread.pool.size}")
    private int poolSize;

    @Bean(name = "similarProductsExecutor")
    public Executor similarProductsExecutor() {

        ThreadPoolTaskExecutor executor
                = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("similar-products-");
        executor.initialize();

        return executor;
    }
}
