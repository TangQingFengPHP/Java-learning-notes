package com.github.mybatis.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysUserCreateRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @Min(0)
    @Max(200)
    private Integer age;
}
