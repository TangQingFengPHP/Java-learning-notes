package com.github.completablefuture.support;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class AsyncMetricsRecorder {

    private final Deque<MetricEvent> recentEvents = new ConcurrentLinkedDeque<>();

    public void recordSuccess(String taskName, Object result) {
        append(new MetricEvent(taskName, "SUCCESS", String.valueOf(result), null, Instant.now()));
    }

    public void recordFailure(String taskName, Throwable ex) {
        append(new MetricEvent(
                taskName,
                "FAILURE",
                null,
                ex == null ? null : ex.getMessage(),
                Instant.now()
        ));
    }

    public List<MetricEvent> recentEvents(int limit) {
        List<MetricEvent> snapshot = new ArrayList<>(recentEvents);
        if (snapshot.size() <= limit) {
            return snapshot;
        }
        return snapshot.subList(snapshot.size() - limit, snapshot.size());
    }

    private void append(MetricEvent event) {
        recentEvents.addLast(event);
        while (recentEvents.size() > 100) {
            recentEvents.pollFirst();
        }
    }

    public record MetricEvent(
            String taskName,
            String status,
            String result,
            String errorMessage,
            Instant recordedAt
    ) {
    }
}
