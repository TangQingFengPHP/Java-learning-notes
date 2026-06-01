package com.github.jdbctemplate.repository;

import com.github.jdbctemplate.entity.User;
import com.github.jdbctemplate.model.UserSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public Long saveAndReturnId(User user) {
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

    public int updateEmail(Long id, String email, LocalDateTime updatedAt) {
        String sql = "update tb_user set email = ?, updated_at = ? where id = ?";
        return jdbcTemplate.update(sql, email, updatedAt, id);
    }

    public int deleteById(Long id) {
        String sql = "delete from tb_user where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Optional<User> findById(Long id) {
        String sql = """
                select %s from tb_user where id = ?
                """.formatted(RowMappers.userSelectColumns());
        List<User> users = jdbcTemplate.query(sql, RowMappers.USER, id);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.getFirst());
    }

    public String findEmailById(Long id) {
        String sql = "select email from tb_user where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public long countAll() {
        String sql = "select count(*) from tb_user";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null ? 0L : count;
    }

    public long countByStatus(String status) {
        String sql = "select count(*) from tb_user where status = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, status);
        return count == null ? 0L : count;
    }

    public List<User> findByStatus(String status) {
        String sql = """
                select %s from tb_user where status = ? order by id desc
                """.formatted(RowMappers.userSelectColumns());
        return jdbcTemplate.query(sql, RowMappers.USER, status);
    }

    public List<User> findPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = """
                select %s from tb_user order by id desc limit ? offset ?
                """.formatted(RowMappers.userSelectColumns());
        return jdbcTemplate.query(sql, RowMappers.USER, pageSize, offset);
    }

    public List<User> findPageByStatus(String status, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = """
                select %s from tb_user where status = ? order by id desc limit ? offset ?
                """.formatted(RowMappers.userSelectColumns());
        return jdbcTemplate.query(sql, RowMappers.USER, status, pageSize, offset);
    }

    public List<User> search(UserSearchRequest req) {
        StringBuilder sql = new StringBuilder("""
                select %s from tb_user where 1 = 1
                """.formatted(RowMappers.userSelectColumns().trim()));

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (req != null) {
            if (req.getUsername() != null && !req.getUsername().isBlank()) {
                sql.append(" and username like :username");
                params.addValue("username", "%" + req.getUsername() + "%");
            }
            if (req.getStatus() != null && !req.getStatus().isBlank()) {
                sql.append(" and status = :status");
                params.addValue("status", req.getStatus());
            }
            if (req.getMinAge() != null) {
                sql.append(" and age >= :minAge");
                params.addValue("minAge", req.getMinAge());
            }
        }

        sql.append(" order by id desc");
        return namedJdbcTemplate.query(sql.toString(), params, RowMappers.USER);
    }

    public List<User> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        String sql = """
                select %s from tb_user where id in (:ids) order by id desc
                """.formatted(RowMappers.userSelectColumns());

        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);
        return namedJdbcTemplate.query(sql, params, RowMappers.USER);
    }

    public List<User> findAllByBeanPropertyMapper() {
        String sql = "select id, username, email, age, status, created_at, updated_at from tb_user order by id desc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public int[] batchUpdateStatus(List<Long> ids, String status) {
        String sql = "update tb_user set status = ?, updated_at = ? where id = ?";
        LocalDateTime now = LocalDateTime.now();
        List<Object[]> batchArgs = ids.stream()
                .map(id -> new Object[]{status, now, id})
                .toList();
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public int[] batchSave(List<User> users) {
        String sql = """
                insert into tb_user (username, email, age, status, created_at)
                values (?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                User user = users.get(i);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getEmail());
                ps.setInt(3, user.getAge());
                ps.setString(4, user.getStatus());
                ps.setObject(5, user.getCreatedAt());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    public int disableByAgeLessThan(int ageExclusive) {
        String sql = "update tb_user set status = ?, updated_at = ? where age < ?";
        return jdbcTemplate.update(sql, "DISABLED", LocalDateTime.now(), ageExclusive);
    }
}
