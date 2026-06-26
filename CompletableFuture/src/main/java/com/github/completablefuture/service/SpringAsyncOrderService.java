package com.github.completablefuture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SpringAsyncOrderService {

    @Async("orderAsyncExecutor")
    public CompletableFuture<String> createOrderAsync(Long userId, Long productId) {
        try {
            Thread.sleep(500);
            return CompletableFuture.completedFuture(
                    "订单创建成功，userId=" + userId + ", productId=" + productId
                            + ", thread=" + Thread.currentThread().getName()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
}
