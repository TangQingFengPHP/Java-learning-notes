package com.github.virtualthread.service;

import com.github.virtualthread.model.LimitedRemoteResult;
import com.github.virtualthread.remote.LimitedRemoteClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class LimitedRemoteService {

    private final LimitedRemoteClient limitedRemoteClient;
    private final ExecutorService virtualThreadExecutor;

    @Value("${app.remote.max-concurrency}")
    private int maxConcurrency;

    public LimitedRemoteResult callWithLimit(int requestCount) throws Exception {
        long start = System.currentTimeMillis();
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < requestCount; i++) {
            futures.add(virtualThreadExecutor.submit(() ->
                    limitedRemoteClient.callSlowEndpoint("/mock/slow?ms=200")
            ));
        }

        long successCount = 0;
        for (Future<String> future : futures) {
            if (future.get() != null) {
                successCount++;
            }
        }

        return new LimitedRemoteResult(
                requestCount,
                successCount,
                System.currentTimeMillis() - start,
                maxConcurrency
        );
    }
}
