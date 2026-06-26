package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LegacyBridgeResponse {
    private String result;
    private String bridgeMode;
    private long costMs;
}
