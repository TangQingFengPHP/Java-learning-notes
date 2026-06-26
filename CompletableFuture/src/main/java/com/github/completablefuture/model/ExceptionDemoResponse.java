package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionDemoResponse {
    private String result;
    private boolean recovered;
}
