package com.github.mybatis.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SysUserSearchRequest {
    private String keyword;
    private String status;

    @Min(0)
    @Max(200)
    private Integer minAge;
}
