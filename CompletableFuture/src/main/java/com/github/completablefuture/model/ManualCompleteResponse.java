package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManualCompleteResponse {
    private String result;
    private String completedBy;
    private long costMs;
}
