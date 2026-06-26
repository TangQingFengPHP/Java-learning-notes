package com.github.completablefuture.repository;

import com.github.completablefuture.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findById(Long id) {
        String sql = """
                select %s from tb_user where id = ?
                """.formatted(RowMappers.userSelectColumns());
        List<User> users = jdbcTemplate.query(sql, RowMappers.USER, id);
        return users.stream().findFirst();
    }
}
