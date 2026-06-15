package com.github.virtualthread.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean(destroyMethod = "close")
    public ExecutorService virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService cpuBoundExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(processors);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    @Bean
    public java.util.concurrent.Semaphore remoteSemaphore(
            @Value("${app.remote.max-concurrency}") int maxConcurrency) {
        return new java.util.concurrent.Semaphore(maxConcurrency);
    }
}
