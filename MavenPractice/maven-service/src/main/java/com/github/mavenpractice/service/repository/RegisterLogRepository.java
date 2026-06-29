package com.github.mavenpractice.service.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegisterLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insert(Long userId, String content) {
        String sql = "insert into tb_register_log (user_id, content, created_at) values (?, ?, ?)";
        jdbcTemplate.update(sql, userId, content, java.time.LocalDateTime.now());
    }
}
