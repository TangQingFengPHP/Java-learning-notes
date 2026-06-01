package com.github.mybatisflex.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserSearchRequest {
    private String username;
    private String status;

    @Min(0)
    @Max(200)
    private Integer minAge;
}

