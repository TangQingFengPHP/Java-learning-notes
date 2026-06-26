package com.github.completablefuture.model;

import com.github.completablefuture.support.AsyncMetricsRecorder.MetricEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WhenCompleteDemoResponse {
    private String result;
    private boolean observedSuccess;
    private List<String> observationLogs;
    private List<MetricEvent> metrics;
    private long costMs;
}
