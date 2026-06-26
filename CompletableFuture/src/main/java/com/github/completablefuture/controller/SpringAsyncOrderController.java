package com.github.completablefuture.controller;

import com.github.completablefuture.service.SpringAsyncOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/async/orders")
@RequiredArgsConstructor
public class SpringAsyncOrderController {

    private final SpringAsyncOrderService springAsyncOrderService;

    @PostMapping
    public CompletableFuture<String> create(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        return springAsyncOrderService.createOrderAsync(userId, productId);
    }
}
