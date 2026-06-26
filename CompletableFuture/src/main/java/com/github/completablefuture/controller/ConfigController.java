package com.github.completablefuture.controller;

import com.github.completablefuture.model.ConfigLoadResponse;
import com.github.completablefuture.service.ConfigLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigLoadService configLoadService;

    @GetMapping("/fastest-anyof")
    public ConfigLoadResponse fastestByAnyOf() {
        return configLoadService.loadFastestByAnyOf();
    }

    @GetMapping("/fastest-either")
    public ConfigLoadResponse fastestByApplyToEither() {
        return configLoadService.loadFastestByApplyToEither();
    }
}
