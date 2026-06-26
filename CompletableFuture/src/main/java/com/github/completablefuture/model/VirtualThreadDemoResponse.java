package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VirtualThreadDemoResponse {
    private String result;
    private boolean virtual;
    private String threadName;
}
