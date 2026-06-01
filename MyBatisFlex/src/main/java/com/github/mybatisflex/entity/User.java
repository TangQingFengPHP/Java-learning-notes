package com.github.mybatisflex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("tb_user")
public class User {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String username;

    private String email;

    private Integer age;

    private String status;

    @Column(isLogicDelete = true)
    private Boolean deleted;

    @Column(version = true)
    private Integer version;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

