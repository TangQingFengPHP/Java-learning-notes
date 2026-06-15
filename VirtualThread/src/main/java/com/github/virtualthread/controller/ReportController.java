package com.github.virtualthread.controller;

import com.github.virtualthread.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{id}/generate")
    public CompletableFuture<Map<String, Object>> generate(@PathVariable Long id) {
        return reportService.generate(id).thenApply(result -> Map.of(
                "reportId", id,
                "result", result,
                "requestThreadVirtual", Thread.currentThread().isVirtual()
        ));
    }
}
