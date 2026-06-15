package com.github.virtualthread.repository;

import com.github.virtualthread.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public Long save(User user) {
        String sql = """
                insert into tb_user (username, email, age, status, created_at)
                values (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getStatus());
            ps.setObject(5, user.getCreatedAt());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    public Optional<User> findById(Long id) {
        String sql = """
                select %s from tb_user where id = ?
                """.formatted(RowMappers.userSelectColumns());
        List<User> users = jdbcTemplate.query(sql, RowMappers.USER, id);
        return users.stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                select %s from tb_user where email = ?
                """.formatted(RowMappers.userSelectColumns());
        List<User> users = jdbcTemplate.query(sql, RowMappers.USER, email);
        return users.stream().findFirst();
    }

    public List<User> findAll() {
        String sql = """
                select %s from tb_user order by id desc
                """.formatted(RowMappers.userSelectColumns());
        return jdbcTemplate.query(sql, RowMappers.USER);
    }

    public long countByStatus(String status) {
        String sql = "select count(*) from tb_user where status = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, status);
        return count == null ? 0L : count;
    }

    public List<User> search(String status, String keyword, int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        StringBuilder sql = new StringBuilder("""
                select %s from tb_user where 1 = 1
                """.formatted(RowMappers.userSelectColumns()));

        MapSqlParameterSource params = new MapSqlParameterSource();
        if (status != null && !status.isBlank()) {
            sql.append(" and status = :status");
            params.addValue("status", status);
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" and username like :keyword");
            params.addValue("keyword", "%" + keyword + "%");
        }
        sql.append(" order by id desc limit :size offset :offset");
        params.addValue("size", size);
        params.addValue("offset", offset);

        return namedJdbcTemplate.query(sql.toString(), params, RowMappers.USER);
    }

    public int disableById(Long id) {
        String sql = "update tb_user set status = ?, updated_at = ? where id = ?";
        return jdbcTemplate.update(sql, "DISABLED", LocalDateTime.now(), id);
    }

    public int deleteById(Long id) {
        String sql = "delete from tb_user where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public String findEmailById(Long id) {
        String sql = "select email from tb_user where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
