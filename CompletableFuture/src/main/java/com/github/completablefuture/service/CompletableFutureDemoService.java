package com.github.completablefuture.service;

import com.github.completablefuture.model.ChainDemoResponse;
import com.github.completablefuture.model.ExceptionDemoResponse;
import com.github.completablefuture.model.VirtualThreadDemoResponse;
import com.github.completablefuture.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class CompletableFutureDemoService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;

    @Qualifier("virtualThreadExecutor")
    private final ExecutorService virtualThreadExecutor;

    private final OrderRepository orderRepository;

    public ChainDemoResponse chainDemo(Long userId) {
        long start = System.currentTimeMillis();

        String result = CompletableFuture.supplyAsync(() -> {
                    sleep(200);
                    return "java";
                }, queryExecutor)
                .thenApply(String::toUpperCase)
                .thenApply(value -> "语言：" + value)
                .thenCompose(value -> CompletableFuture.supplyAsync(
                        () -> value + " | 订单数=" + orderRepository.findRecentByUserId(userId, 10).size(),
                        queryExecutor
                ))
                .join();

        return new ChainDemoResponse(
                result,
                Thread.currentThread().getName(),
                System.currentTimeMillis() - start
        );
    }

    public ExceptionDemoResponse exceptionDemo(boolean fail, String mode) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (fail) {
                throw new IllegalStateException("库存服务不可用");
            }
            return "支付成功";
        }, queryExecutor);

        String result;
        boolean recovered = fail;

        if ("exceptionally".equalsIgnoreCase(mode)) {
            result = future.exceptionally(ex -> "库存状态未知").join();
        } else {
            result = future.handle((value, ex) -> ex != null ? "支付状态待确认" : value).join();
        }

        return new ExceptionDemoResponse(result, recovered);
    }

    public VirtualThreadDemoResponse virtualThreadDemo() {
        String result = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return "虚拟线程执行结果";
        }, virtualThreadExecutor).join();

        Thread current = Thread.currentThread();
        return new VirtualThreadDemoResponse(
                result,
                current.isVirtual(),
                current.toString()
        );
    }

    public void runFireAndForget() {
        CompletableFuture.runAsync(() -> {
            sleep(100);
            System.out.println("[runAsync] 后台刷新缓存完成，线程=" + Thread.currentThread().getName());
        }, queryExecutor)
                .thenRun(() -> System.out.println("[thenRun] 缓存刷新后续动作"))
                .thenAccept(ignore -> {
                });
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
