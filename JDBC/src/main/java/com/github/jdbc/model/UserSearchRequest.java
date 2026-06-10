package com.github.jdbc.model;

import lombok.Data;

@Data
public class UserSearchRequest {
    private String status;
    private Integer minAge;
}
