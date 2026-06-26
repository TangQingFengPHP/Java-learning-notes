package com.github.completablefuture.service;

import com.github.completablefuture.model.ConfigLoadResponse;
import com.github.completablefuture.remote.HttpRemoteClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class ConfigLoadService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;
    private final HttpRemoteClient httpRemoteClient;
    private final ObjectMapper objectMapper;

    public ConfigLoadResponse loadFastestByAnyOf() {
        long start = System.currentTimeMillis();

        CompletableFuture<String> primaryFuture = loadFromPrimary();
        CompletableFuture<String> backupFuture = loadFromBackup();

        Object fastest = CompletableFuture.anyOf(primaryFuture, backupFuture).join();
        String config = fastest.toString();

        return new ConfigLoadResponse(
                config.contains("primary") ? "primary" : "backup",
                config,
                System.currentTimeMillis() - start
        );
    }

    public ConfigLoadResponse loadFastestByApplyToEither() {
        long start = System.currentTimeMillis();

        CompletableFuture<String> primaryFuture = loadFromPrimary();
        CompletableFuture<String> backupFuture = loadFromBackup();

        String config = primaryFuture
                .applyToEither(backupFuture, value -> value)
                .join();

        return new ConfigLoadResponse(
                config.contains("primary") ? "primary" : "backup",
                config,
                System.currentTimeMillis() - start
        );
    }

    private CompletableFuture<String> loadFromPrimary() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return httpRemoteClient.getConfigFromPrimary();
            } catch (Exception e) {
                throw new IllegalStateException("主配置中心不可用", e);
            }
        }, queryExecutor).thenApply(this::extractConfig);
    }

    private CompletableFuture<String> loadFromBackup() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return httpRemoteClient.getConfigFromBackup();
            } catch (Exception e) {
                throw new IllegalStateException("备配置中心不可用", e);
            }
        }, queryExecutor).thenApply(this::extractConfig);
    }

    private String extractConfig(String body) {
        try {
            JsonNode node = objectMapper.readTree(body);
            return node.get("config").asText();
        } catch (Exception e) {
            throw new IllegalStateException("配置解析失败", e);
        }
    }
}
