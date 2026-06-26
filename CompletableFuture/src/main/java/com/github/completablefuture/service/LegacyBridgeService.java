package com.github.completablefuture.service;

import com.github.completablefuture.model.LegacyBridgeResponse;
import com.github.completablefuture.model.ManualCompleteResponse;
import com.github.completablefuture.remote.LegacyCallbackClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class LegacyBridgeService {

    private final LegacyCallbackClient legacyCallbackClient;

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;

    public LegacyBridgeResponse bridgeLegacyCallback(Long userId, boolean fail, long delayMs) {
        long start = System.currentTimeMillis();

        String result = bridgeToCompletableFuture(userId, fail, delayMs)
                .thenApply(profile -> profile + " | 已由 CompletableFuture 继续编排")
                .join();

        return new LegacyBridgeResponse(
                result,
                "legacy-callback -> complete/completeExceptionally",
                System.currentTimeMillis() - start
        );
    }

    public CompletableFuture<String> bridgeToCompletableFuture(Long userId, boolean fail, long delayMs) {
        CompletableFuture<String> future = new CompletableFuture<>();

        legacyCallbackClient.fetchUserProfileAsync(userId, fail, delayMs, new com.github.completablefuture.remote.LegacyCallback<>() {
            @Override
            public void onSuccess(String result) {
                future.complete(result);
            }

            @Override
            public void onError(Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    public ManualCompleteResponse manualComplete(long delayMs) {
        long start = System.currentTimeMillis();
        CompletableFuture<String> future = new CompletableFuture<>();

        queryExecutor.execute(() -> {
            try {
                Thread.sleep(delayMs);
                future.complete("手动填入的回调结果");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            }
        });

        String result = future.join();
        return new ManualCompleteResponse(
                result,
                "queryExecutor 线程在 " + delayMs + "ms 后调用 complete()",
                System.currentTimeMillis() - start
        );
    }
}
