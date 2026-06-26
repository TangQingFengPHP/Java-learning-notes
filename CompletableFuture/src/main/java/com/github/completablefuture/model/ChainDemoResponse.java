package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainDemoResponse {
    private String result;
    private String threadName;
    private long costMs;
}
