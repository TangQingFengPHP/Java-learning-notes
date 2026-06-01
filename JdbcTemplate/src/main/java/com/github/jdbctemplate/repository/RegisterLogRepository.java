package com.github.jdbctemplate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class RegisterLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insert(Long userId, String content) {
        String sql = "insert into tb_register_log (user_id, content, created_at) values (?, ?, ?)";
        jdbcTemplate.update(sql, userId, content, LocalDateTime.now());
    }
}
