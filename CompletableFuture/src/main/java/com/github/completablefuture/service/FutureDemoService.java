package com.github.completablefuture.service;

import com.github.completablefuture.model.FutureBasicResponse;
import com.github.completablefuture.model.FutureTimeoutResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class FutureDemoService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;

    public FutureBasicResponse basicDemo() throws Exception {
        long start = System.currentTimeMillis();

        Callable<Integer> task = () -> {
            Thread.sleep(300);
            return 100 + 200;
        };

        Future<Integer> future = queryExecutor.submit(task);
        Integer result = future.get();

        return new FutureBasicResponse(
                "任务已提交，主流程继续执行后通过 get() 获取结果",
                result,
                System.currentTimeMillis() - start
        );
    }

    public FutureTimeoutResponse timeoutDemo(long taskMs, long timeoutMs) throws Exception {
        long start = System.currentTimeMillis();

        Future<String> future = queryExecutor.submit(() -> {
            try {
                Thread.sleep(taskMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "任务被取消";
            }
            return "远程接口结果";
        });

        try {
            String result = future.get(timeoutMs, TimeUnit.MILLISECONDS);
            return new FutureTimeoutResponse(
                    true,
                    result,
                    future.isCancelled(),
                    System.currentTimeMillis() - start
            );
        } catch (TimeoutException e) {
            future.cancel(true);
            return new FutureTimeoutResponse(
                    false,
                    "任务超时，已尝试取消",
                    future.isCancelled(),
                    System.currentTimeMillis() - start
            );
        }
    }
}
