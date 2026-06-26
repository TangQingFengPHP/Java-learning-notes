package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FutureTimeoutResponse {
    private boolean success;
    private String result;
    private boolean cancelled;
    private long costMs;
}
