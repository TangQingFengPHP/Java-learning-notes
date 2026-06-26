package com.github.completablefuture.controller;

import com.github.completablefuture.model.ChainDemoResponse;
import com.github.completablefuture.model.ExceptionDemoResponse;
import com.github.completablefuture.model.LegacyBridgeResponse;
import com.github.completablefuture.model.ManualCompleteResponse;
import com.github.completablefuture.model.VirtualThreadDemoResponse;
import com.github.completablefuture.model.WhenCompleteDemoResponse;
import com.github.completablefuture.service.CompletableFutureDemoService;
import com.github.completablefuture.service.LegacyBridgeService;
import com.github.completablefuture.service.WhenCompleteDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class CompletableFutureDemoController {

    private final CompletableFutureDemoService demoService;
    private final WhenCompleteDemoService whenCompleteDemoService;
    private final LegacyBridgeService legacyBridgeService;

    @GetMapping("/chain")
    public ChainDemoResponse chain(@RequestParam(defaultValue = "1") Long userId) {
        return demoService.chainDemo(userId);
    }

    @GetMapping("/exception")
    public ExceptionDemoResponse exception(
            @RequestParam(defaultValue = "true") boolean fail,
            @RequestParam(defaultValue = "exceptionally") String mode) {
        return demoService.exceptionDemo(fail, mode);
    }

    @GetMapping("/virtual-thread")
    public VirtualThreadDemoResponse virtualThread() {
        return demoService.virtualThreadDemo();
    }

    @GetMapping("/fire-and-forget")
    public String fireAndForget() {
        demoService.runFireAndForget();
        return "runAsync 后台任务已提交";
    }

    @GetMapping("/when-complete")
    public WhenCompleteDemoResponse whenComplete(@RequestParam(defaultValue = "false") boolean fail) {
        return whenCompleteDemoService.observeDemo(fail);
    }

    @GetMapping("/legacy-bridge")
    public LegacyBridgeResponse legacyBridge(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "false") boolean fail,
            @RequestParam(defaultValue = "300") long delayMs) {
        return legacyBridgeService.bridgeLegacyCallback(userId, fail, delayMs);
    }

    @GetMapping("/manual-complete")
    public ManualCompleteResponse manualComplete(@RequestParam(defaultValue = "500") long delayMs) {
        return legacyBridgeService.manualComplete(delayMs);
    }
}
