package com.github.completablefuture.controller;

import com.github.completablefuture.model.FutureBasicResponse;
import com.github.completablefuture.model.FutureTimeoutResponse;
import com.github.completablefuture.service.FutureDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/future")
@RequiredArgsConstructor
public class FutureDemoController {

    private final FutureDemoService futureDemoService;

    @GetMapping("/basic")
    public FutureBasicResponse basic() throws Exception {
        return futureDemoService.basicDemo();
    }

    @GetMapping("/timeout")
    public FutureTimeoutResponse timeout(
            @RequestParam(defaultValue = "3000") long taskMs,
            @RequestParam(defaultValue = "1000") long timeoutMs) throws Exception {
        return futureDemoService.timeoutDemo(taskMs, timeoutMs);
    }
}
