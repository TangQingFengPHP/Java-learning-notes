package com.github.virtualthread.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailResponse {
    private UserResponse user;
    private String orders;
    private String account;
    private long activeUserCount;
    private long elapsedMs;
}
