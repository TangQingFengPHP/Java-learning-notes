package com.github.virtualthread.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LimitedRemoteResult {
    private int requestCount;
    private long successCount;
    private long elapsedMs;
    private int maxConcurrency;
}
