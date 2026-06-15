package com.github.virtualthread.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BatchQueryResult {
    private int taskCount;
    private long successCount;
    private long elapsedMs;
    private boolean requestThreadVirtual;
}
