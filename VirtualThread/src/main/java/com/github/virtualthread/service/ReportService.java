package com.github.virtualthread.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ReportService {

    @Async
    public CompletableFuture<String> generate(Long reportId) {
        try {
            Thread.sleep(1000);
            String result = "报表生成完成，id=" + reportId
                    + "，virtual=" + Thread.currentThread().isVirtual();
            log.info("异步报表任务完成：{}", result);
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }
}
