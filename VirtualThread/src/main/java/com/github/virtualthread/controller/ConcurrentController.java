package com.github.virtualthread.controller;

import com.github.virtualthread.model.BatchQueryResult;
import com.github.virtualthread.model.LimitedRemoteResult;
import com.github.virtualthread.service.ConcurrentQueryService;
import com.github.virtualthread.service.LimitedRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concurrent")
@RequiredArgsConstructor
public class ConcurrentController {

    private final ConcurrentQueryService concurrentQueryService;
    private final LimitedRemoteService limitedRemoteService;

    @GetMapping("/batch-query")
    public BatchQueryResult batchQuery(@RequestParam(defaultValue = "100") int count) throws Exception {
        int safeCount = Math.min(Math.max(count, 1), 5000);
        return concurrentQueryService.batchQueryByVirtualThreads(safeCount);
    }

    @GetMapping("/limited-remote")
    public LimitedRemoteResult limitedRemote(@RequestParam(defaultValue = "50") int count) throws Exception {
        int safeCount = Math.min(Math.max(count, 1), 500);
        return limitedRemoteService.callWithLimit(safeCount);
    }
}
