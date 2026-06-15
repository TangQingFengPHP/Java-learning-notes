package com.github.virtualthread.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThreadInfoResponse {
    private String thread;
    private String name;
    private boolean virtual;
    private String traceId;
}
