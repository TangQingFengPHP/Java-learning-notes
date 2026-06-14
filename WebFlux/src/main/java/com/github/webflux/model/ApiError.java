package com.github.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {

    private Integer code;
    private String message;
    private String timestamp;

    public static ApiError of(int code, String message) {
        return new ApiError(code, message, java.time.LocalDateTime.now().toString());
    }
}
