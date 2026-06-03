package com.github.springdatajpa.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateEmailRequest {
    @NotBlank
    @Email
    private String email;
}
