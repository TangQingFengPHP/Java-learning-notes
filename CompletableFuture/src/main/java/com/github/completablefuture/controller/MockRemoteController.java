package com.github.completablefuture.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockRemoteController {

    @GetMapping("/coupons")
    public Map<String, Object> coupons(@RequestParam Long userId) throws InterruptedException {
        Thread.sleep(500);
        return Map.of(
                "userId", userId,
                "count", 3,
                "thread", Thread.currentThread().toString()
        );
    }

    @GetMapping("/accounts/{userId}")
    public Map<String, Object> account(@PathVariable Long userId) throws InterruptedException {
        Thread.sleep(300);
        return Map.of(
                "userId", userId,
                "balance", 1288.50,
                "thread", Thread.currentThread().toString()
        );
    }

    @GetMapping("/config/primary")
    public Map<String, Object> primaryConfig() throws InterruptedException {
        Thread.sleep(500);
        return Map.of(
                "source", "primary",
                "config", "primary-config-v1",
                "thread", Thread.currentThread().toString()
        );
    }

    @GetMapping("/config/backup")
    public Map<String, Object> backupConfig() throws InterruptedException {
        Thread.sleep(200);
        return Map.of(
                "source", "backup",
                "config", "backup-config-v1",
                "thread", Thread.currentThread().toString()
        );
    }

    @GetMapping("/slow")
    public Map<String, Object> slow(@RequestParam(defaultValue = "2000") long ms) throws InterruptedException {
        Thread.sleep(ms);
        return Map.of(
                "message", "slow response",
                "sleepMs", ms,
                "thread", Thread.currentThread().toString()
        );
    }
}
