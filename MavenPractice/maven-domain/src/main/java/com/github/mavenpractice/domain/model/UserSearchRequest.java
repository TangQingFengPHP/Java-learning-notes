package com.github.mavenpractice.domain.model;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserSearchRequest {
    private String username;
    private String status;

    @Min(0)
    private Integer minAge;
}
