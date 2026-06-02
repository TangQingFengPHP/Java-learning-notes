package com.github.mybatisplus.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysUserUpdateEmailRequest {
    @NotBlank
    @Email
    private String email;
}

