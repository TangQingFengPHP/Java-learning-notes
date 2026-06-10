package com.github.jdbc.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account {
    private Long id;
    private String ownerName;
    private BigDecimal balance;
    private LocalDateTime updatedAt;
}
