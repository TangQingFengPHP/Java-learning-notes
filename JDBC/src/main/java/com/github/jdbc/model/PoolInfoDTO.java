package com.github.jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PoolInfoDTO {
    private String poolName;
    private String dataSourceType;
    private int maximumPoolSize;
    private int minimumIdle;
    private int activeConnections;
    private int idleConnections;
    private int totalConnections;
    private int threadsAwaitingConnection;
}
