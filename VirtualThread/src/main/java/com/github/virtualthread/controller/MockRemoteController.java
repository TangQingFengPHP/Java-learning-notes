package com.github.virtualthread.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockRemoteController {

    @GetMapping("/orders")
    public Map<String, Object> orders(@RequestParam Long userId) throws InterruptedException {
        Thread.sleep(200);
        return Map.of(
                "userId", userId,
                "orderCount", 2,
                "thread", Thread.currentThread().toString(),
                "virtual", Thread.currentThread().isVirtual()
        );
    }

    @GetMapping("/accounts/{userId}")
    public Map<String, Object> account(@PathVariable Long userId) throws InterruptedException {
        Thread.sleep(300);
        return Map.of(
                "userId", userId,
                "balance", 1288.50,
                "thread", Thread.currentThread().toString(),
                "virtual", Thread.currentThread().isVirtual()
        );
    }

    @GetMapping("/slow")
    public Map<String, Object> slow(@RequestParam(defaultValue = "200") long ms) throws InterruptedException {
        Thread.sleep(ms);
        return Map.of(
                "message", "slow response",
                "sleepMs", ms,
                "thread", Thread.currentThread().toString(),
                "virtual", Thread.currentThread().isVirtual()
        );
    }
}
