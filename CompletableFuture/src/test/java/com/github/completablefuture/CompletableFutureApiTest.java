package com.github.completablefuture;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompletableFutureApiTest {

    @Test
    void thenApplyChain() {
        String result = CompletableFuture.supplyAsync(() -> "java")
                .thenApply(String::toUpperCase)
                .thenApply(value -> "Hello " + value)
                .join();

        assertEquals("Hello JAVA", result);
    }

    @Test
    void allOfCollectsResults() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<CompletableFuture<String>> futures = ids.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> "商品-" + id))
                .toList();

        List<String> products = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(ignore -> futures.stream().map(CompletableFuture::join).toList())
                .join();

        assertEquals(3, products.size());
        assertEquals("商品-1", products.get(0));
    }

    @Test
    void completeOnTimeoutReturnsDefault() {
        String result = CompletableFuture.supplyAsync(() -> {
                    sleep(500);
                    return "慢接口";
                })
                .completeOnTimeout("默认结果", 50, TimeUnit.MILLISECONDS)
                .join();

        assertEquals("默认结果", result);
    }

    @Test
    void exceptionallyRecovers() {
        String result = CompletableFuture.<String>supplyAsync(() -> {
                    throw new IllegalStateException("服务不可用");
                })
                .exceptionally(ex -> "兜底值")
                .join();

        assertEquals("兜底值", result);
    }

    @Test
    void anyOfReturnsFastest() {
        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return "slow";
        });
        CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return "fast";
        });

        Object winner = CompletableFuture.anyOf(slow, fast).join();
        assertEquals("fast", winner);
    }

    @Test
    void applyToEitherKeepsGenericType() {
        CompletableFuture<String> primary = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "primary";
        });
        CompletableFuture<String> backup = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return "backup";
        });

        String result = primary.applyToEither(backup, value -> "使用：" + value).join();
        assertTrue(result.contains("backup"));
    }

    @Test
    void whenCompleteObservesWithoutChangingResult() {
        List<String> logs = new java.util.ArrayList<>();

        String result = CompletableFuture.supplyAsync(() -> "OK")
                .whenComplete((value, ex) -> logs.add("observed:" + value))
                .thenApply(String::toLowerCase)
                .join();

        assertEquals("ok", result);
        assertEquals(1, logs.size());
        assertEquals("observed:OK", logs.get(0));
    }

    @Test
    void legacyCallbackBridgeCompletesFuture() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();

        boolean fail = false;
        long delayMs = 50L;
        Thread callbackThread = new Thread(() -> {
            try {
                Thread.sleep(delayMs);
                if (fail) {
                    future.completeExceptionally(new IllegalStateException("legacy failed"));
                } else {
                    future.complete("legacy-success");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            }
        });
        callbackThread.start();

        assertEquals("legacy-success", future.get(1, TimeUnit.SECONDS));
    }

    @Test
    void manualCompleteFillsResultLater() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();

        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(80);
                future.complete("manual-result");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            }
        });
        worker.start();

        assertEquals("manual-result", future.get(1, TimeUnit.SECONDS));
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
