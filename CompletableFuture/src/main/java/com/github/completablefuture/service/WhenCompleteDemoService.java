package com.github.completablefuture.service;

import com.github.completablefuture.model.LegacyBridgeResponse;
import com.github.completablefuture.model.ManualCompleteResponse;
import com.github.completablefuture.model.WhenCompleteDemoResponse;
import com.github.completablefuture.remote.LegacyCallbackClient;
import com.github.completablefuture.support.AsyncMetricsRecorder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class WhenCompleteDemoService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;

    private final AsyncMetricsRecorder metricsRecorder;

    public WhenCompleteDemoResponse observeDemo(boolean fail) {
        long start = System.currentTimeMillis();
        List<String> observationLogs = new ArrayList<>();

        String result = CompletableFuture.supplyAsync(() -> {
                    sleep(200);
                    if (fail) {
                        throw new IllegalStateException("远程库存查询失败");
                    }
                    return "库存充足";
                }, queryExecutor)
                .whenComplete((value, ex) -> {
                    if (ex != null) {
                        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                        observationLogs.add("whenComplete: 记录失败，ex=" + cause.getMessage());
                        metricsRecorder.recordFailure("inventory-query", cause);
                    } else {
                        observationLogs.add("whenComplete: 记录成功，value=" + value);
                        metricsRecorder.recordSuccess("inventory-query", value);
                    }
                })
                .handle((value, ex) -> {
                    if (ex != null) {
                        return "库存状态待确认";
                    }
                    return "【业务结果】" + value;
                })
                .join();

        return new WhenCompleteDemoResponse(
                result,
                !fail,
                observationLogs,
                metricsRecorder.recentEvents(5),
                System.currentTimeMillis() - start
        );
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
