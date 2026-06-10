package com.github.jdbc.dao;

import com.github.jdbc.entity.User;
import com.github.jdbc.mapper.UserRowMapper;
import com.github.jdbc.support.JdbcSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcSupport jdbcSupport;

    public Long insert(User user) {
        String sql = """
                insert into users (username, email, age, status, created_at)
                values (?, ?, ?, ?, ?)
                """;

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setInt(3, user.getAge());
            statement.setString(4, user.getStatus());
            statement.setObject(5, user.getCreatedAt());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            return null;
        } catch (SQLException ex) {
            if (isDuplicateKey(ex)) {
                throw new DuplicateKeyException("邮箱已存在", ex);
            }
            throw new IllegalStateException("插入用户失败", ex);
        }
    }

    public Long registerWithLog(User user, String logContent) {
        String userSql = """
                insert into users (username, email, age, status, created_at)
                values (?, ?, ?, ?, ?)
                """;
        String logSql = """
                insert into operation_log (user_id, content, created_at)
                values (?, ?, ?)
                """;

        try (Connection connection = jdbcSupport.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userStatement = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, user.getUsername());
                userStatement.setString(2, user.getEmail());
                userStatement.setInt(3, user.getAge());
                userStatement.setString(4, user.getStatus());
                userStatement.setObject(5, user.getCreatedAt());
                userStatement.executeUpdate();

                Long userId;
                try (ResultSet keys = userStatement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new IllegalStateException("未获取到自增主键");
                    }
                    userId = keys.getLong(1);
                }

                try (PreparedStatement logStatement = connection.prepareStatement(logSql)) {
                    logStatement.setLong(1, userId);
                    logStatement.setString(2, logContent);
                    logStatement.setObject(3, LocalDateTime.now());
                    logStatement.executeUpdate();
                }

                connection.commit();
                return userId;
            } catch (Exception ex) {
                connection.rollback();
                if (ex instanceof SQLException sqlEx && isDuplicateKey(sqlEx)) {
                    throw new DuplicateKeyException("邮箱已存在", ex);
                }
                throw ex instanceof RuntimeException runtimeEx
                        ? runtimeEx
                        : new IllegalStateException("注册用户失败", ex);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("注册用户失败", ex);
        }
    }

    public Optional<User> findById(Long id) {
        String sql = """
                select %s from users where id = ?
                """.formatted(UserRowMapper.SELECT_COLUMNS);

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(UserRowMapper.map(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("查询用户失败", ex);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                select %s from users where email = ?
                """.formatted(UserRowMapper.SELECT_COLUMNS);

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(UserRowMapper.map(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("按邮箱查询失败", ex);
        }
    }

    public List<User> search(String status, Integer minAge) {
        StringBuilder sql = new StringBuilder("""
                select %s from users where 1 = 1
                """.formatted(UserRowMapper.SELECT_COLUMNS.trim()));

        List<Object> args = new ArrayList<>();
        if (status != null && !status.isBlank()) {
            sql.append(" and status = ?");
            args.add(status);
        }
        if (minAge != null) {
            sql.append(" and age >= ?");
            args.add(minAge);
        }
        sql.append(" order by id desc");

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            bindArgs(statement, args);
            List<User> users = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    users.add(UserRowMapper.map(rs));
                }
            }
            return users;
        } catch (SQLException ex) {
            throw new IllegalStateException("条件查询失败", ex);
        }
    }

    public List<User> findPage(String status, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = """
                select %s from users
                where status = ?
                order by id desc
                limit ? offset ?
                """.formatted(UserRowMapper.SELECT_COLUMNS);

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, pageSize);
            statement.setInt(3, offset);

            List<User> users = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    users.add(UserRowMapper.map(rs));
                }
            }
            return users;
        } catch (SQLException ex) {
            throw new IllegalStateException("分页查询失败", ex);
        }
    }

    public long countByStatus(String status) {
        String sql = "select count(*) from users where status = ?";

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("统计用户失败", ex);
        }
    }

    public int updateEmail(Long id, String email) {
        String sql = "update users set email = ? where id = ?";

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setLong(2, id);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            if (isDuplicateKey(ex)) {
                throw new DuplicateKeyException("邮箱已存在", ex);
            }
            throw new IllegalStateException("更新邮箱失败", ex);
        }
    }

    public int deleteById(Long id) {
        String sql = "delete from users where id = ?";

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("删除用户失败", ex);
        }
    }

    public int[] batchInsert(List<User> users) {
        String sql = """
                insert into users (username, email, age, status, created_at)
                values (?, ?, ?, ?, ?)
                """;

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);
            try {
                for (User user : users) {
                    statement.setString(1, user.getUsername());
                    statement.setString(2, user.getEmail());
                    statement.setInt(3, user.getAge());
                    statement.setString(4, user.getStatus());
                    statement.setObject(5, user.getCreatedAt());
                    statement.addBatch();
                }
                int[] result = statement.executeBatch();
                connection.commit();
                return result;
            } catch (Exception ex) {
                connection.rollback();
                throw ex instanceof RuntimeException runtimeEx
                        ? runtimeEx
                        : new IllegalStateException("批量插入失败", ex);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("批量插入失败", ex);
        }
    }

    private static void bindArgs(PreparedStatement statement, List<Object> args) throws SQLException {
        for (int i = 0; i < args.size(); i++) {
            statement.setObject(i + 1, args.get(i));
        }
    }

    private static boolean isDuplicateKey(SQLException ex) {
        return ex.getErrorCode() == 1062 || "23000".equals(ex.getSQLState());
    }
}
