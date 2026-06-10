package com.github.jdbc.service;

import com.github.jdbc.dao.AccountDao;
import com.github.jdbc.dao.OrderDao;
import com.github.jdbc.entity.Account;
import com.github.jdbc.model.OrderUserDTO;
import com.github.jdbc.model.PoolInfoDTO;
import com.github.jdbc.model.TransferRequest;
import com.github.jdbc.support.JdbcSupport;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcMetaService {

    private final JdbcSupport jdbcSupport;
    private final AccountDao accountDao;
    private final OrderDao orderDao;

    public PoolInfoDTO poolInfo() {
        DataSource dataSource = jdbcSupport.getDataSource();
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            HikariPoolMXBean pool = hikariDataSource.getHikariPoolMXBean();
            return new PoolInfoDTO(
                    hikariDataSource.getPoolName(),
                    "HikariCP",
                    hikariDataSource.getMaximumPoolSize(),
                    hikariDataSource.getMinimumIdle(),
                    pool == null ? 0 : pool.getActiveConnections(),
                    pool == null ? 0 : pool.getIdleConnections(),
                    pool == null ? 0 : pool.getTotalConnections(),
                    pool == null ? 0 : pool.getThreadsAwaitingConnection()
            );
        }
        return new PoolInfoDTO("unknown", dataSource.getClass().getSimpleName(), 0, 0, 0, 0, 0, 0);
    }

    public List<Account> listAccounts() {
        return accountDao.findAll();
    }

    public void transfer(TransferRequest request) {
        accountDao.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
    }

    public List<OrderUserDTO> findOrderUserByStatus(String status) {
        return orderDao.findOrderUserByStatus(status);
    }
}
