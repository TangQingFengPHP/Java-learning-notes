package com.github.virtualthread.repository;

import com.github.virtualthread.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;

public final class RowMappers {

    private RowMappers() {
    }

    public static final RowMapper<User> USER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setAge(rs.getInt("age"));
        user.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return user;
    };

    public static String userSelectColumns() {
        return "id, username, email, age, status, created_at, updated_at";
    }
}
