package com.github.virtualthread.service;

import com.github.virtualthread.model.BatchQueryResult;
import com.github.virtualthread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class ConcurrentQueryService {

    private final UserRepository userRepository;
    private final ExecutorService virtualThreadExecutor;

    public BatchQueryResult batchQueryByVirtualThreads(int taskCount) throws Exception {
        boolean requestThreadVirtual = Thread.currentThread().isVirtual();
        long start = System.currentTimeMillis();
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            long userId = (i % 3) + 1;
            futures.add(virtualThreadExecutor.submit(() -> {
                userRepository.findById(userId);
                return true;
            }));
        }

        long successCount = 0;
        for (Future<Boolean> future : futures) {
            if (Boolean.TRUE.equals(future.get())) {
                successCount++;
            }
        }

        return new BatchQueryResult(
                taskCount,
                successCount,
                System.currentTimeMillis() - start,
                requestThreadVirtual
        );
    }
}
