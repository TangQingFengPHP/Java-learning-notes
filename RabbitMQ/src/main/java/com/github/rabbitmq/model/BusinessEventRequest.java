package com.github.rabbitmq.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BusinessEventRequest {

    @NotBlank
    private String routingKey;

    @NotBlank
    private String content;
}
