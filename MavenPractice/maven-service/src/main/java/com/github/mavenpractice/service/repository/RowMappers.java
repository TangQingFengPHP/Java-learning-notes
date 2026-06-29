package com.github.mavenpractice.service.repository;

import com.github.mavenpractice.domain.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RowMappers {

    public static final RowMapper<User> USER = (rs, rowNum) -> mapUser(rs);

    private RowMappers() {
    }

    public static String userSelectColumns() {
        return "id, username, email, age, status, created_at, updated_at";
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setAge(rs.getInt("age"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        var updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return user;
    }
}
