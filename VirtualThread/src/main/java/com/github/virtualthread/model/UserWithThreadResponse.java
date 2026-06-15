package com.github.virtualthread.model;

import com.github.virtualthread.entity.User;
import lombok.Data;

@Data
public class UserWithThreadResponse {
    private String thread;
    private boolean virtual;
    private String traceId;
    private UserResponse user;

    public static UserWithThreadResponse of(User user, String traceId) {
        Thread current = Thread.currentThread();
        UserWithThreadResponse response = new UserWithThreadResponse();
        response.setThread(current.toString());
        response.setVirtual(current.isVirtual());
        response.setTraceId(traceId);
        response.setUser(UserResponse.from(user));
        return response;
    }
}
