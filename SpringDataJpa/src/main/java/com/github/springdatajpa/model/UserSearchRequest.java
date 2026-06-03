package com.github.springdatajpa.model;

import lombok.Data;

@Data
public class UserSearchRequest {
    private String keyword;
    private String status;
    private Integer minAge;
}
