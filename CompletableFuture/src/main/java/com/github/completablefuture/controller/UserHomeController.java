package com.github.completablefuture.controller;

import com.github.completablefuture.model.UserHomeModels.UserHomeResponse;
import com.github.completablefuture.service.UserHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserHomeController {

    private final UserHomeService userHomeService;

    @GetMapping("/{id}/home")
    public CompletableFuture<UserHomeResponse> home(@PathVariable Long id) {
        return userHomeService.loadHomeAsync(id);
    }
}
