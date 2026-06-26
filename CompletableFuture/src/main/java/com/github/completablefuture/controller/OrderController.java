package com.github.completablefuture.controller;

import com.github.completablefuture.model.OrderModels.OrderCreateCommand;
import com.github.completablefuture.model.OrderModels.OrderCreateResult;
import com.github.completablefuture.service.OrderCreateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCreateService orderCreateService;

    @PostMapping
    public CompletableFuture<OrderCreateResult> create(@Valid @RequestBody OrderCreateCommand command) {
        return orderCreateService.createAsync(command);
    }
}
