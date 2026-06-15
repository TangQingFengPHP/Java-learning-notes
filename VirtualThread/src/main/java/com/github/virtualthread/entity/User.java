package com.github.virtualthread.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private Integer age;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
