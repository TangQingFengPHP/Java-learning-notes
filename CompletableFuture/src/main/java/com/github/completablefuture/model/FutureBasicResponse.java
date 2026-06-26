package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FutureBasicResponse {
    private String message;
    private Integer result;
    private long costMs;
}
