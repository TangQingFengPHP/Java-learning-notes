package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigLoadResponse {
    private String source;
    private String config;
    private long costMs;
}
