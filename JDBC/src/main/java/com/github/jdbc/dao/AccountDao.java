package com.github.jdbc.dao;

import com.github.jdbc.entity.Account;
import com.github.jdbc.mapper.UserRowMapper;
import com.github.jdbc.support.JdbcSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountDao {

    private final JdbcSupport jdbcSupport;

    public List<Account> findAll() {
        String sql = "select id, owner_name, balance, updated_at from accounts order by id";

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            List<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                accounts.add(UserRowMapper.mapAccount(rs));
            }
            return accounts;
        } catch (SQLException ex) {
            throw new IllegalStateException("查询账户失败", ex);
        }
    }

    public Optional<Account> findById(Long id) {
        String sql = "select id, owner_name, balance, updated_at from accounts where id = ?";

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(UserRowMapper.mapAccount(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("查询账户失败", ex);
        }
    }

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        String decreaseSql = "update accounts set balance = balance - ?, updated_at = ? where id = ? and balance >= ?";
        String increaseSql = "update accounts set balance = balance + ?, updated_at = ? where id = ?";

        try (Connection connection = jdbcSupport.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try (PreparedStatement decrease = connection.prepareStatement(decreaseSql);
                 PreparedStatement increase = connection.prepareStatement(increaseSql)) {

                LocalDateTime now = LocalDateTime.now();

                decrease.setBigDecimal(1, amount);
                decrease.setObject(2, now);
                decrease.setLong(3, fromAccountId);
                decrease.setBigDecimal(4, amount);
                int decreased = decrease.executeUpdate();
                if (decreased == 0) {
                    throw new IllegalArgumentException("转出账户不存在或余额不足");
                }

                increase.setBigDecimal(1, amount);
                increase.setObject(2, now);
                increase.setLong(3, toAccountId);
                int increased = increase.executeUpdate();
                if (increased == 0) {
                    throw new IllegalArgumentException("转入账户不存在");
                }

                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
                throw ex instanceof RuntimeException runtimeEx
                        ? runtimeEx
                        : new IllegalStateException("转账失败", ex);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("转账失败", ex);
        }
    }
}
