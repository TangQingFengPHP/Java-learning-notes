# JDBC 实战模块（原生 JDBC）

## 技术栈

- JDK 21
- Spring Boot 3.4.x（仅用于 Web 层 + **HikariCP 数据源注入**，数据访问手写 JDBC）
- MySQL 8.4
- Lombok
- Docker / docker-compose

## 与 `JdbcTemplate/` 模块的区别

| 模块 | 数据访问方式 |
| --- | --- |
| **JDBC/**（本模块） | 手写 `Connection` / `PreparedStatement` / `ResultSet` |
| **JdbcTemplate/** | Spring 封装的 `JdbcTemplate` / `NamedParameterJdbcTemplate` |

两者底层都走 JDBC + HikariCP，本模块更贴近文档中的底层 API 写法。

## 覆盖知识点（与文档对齐）

| 知识点 | 示例位置 |
| --- | --- |
| `DataSource` + HikariCP | `JdbcSupport`、`GET /jdbc/pool` |
| `PreparedStatement` + `?` 占位符 | `UserDao` |
| `ResultSet` + RowMapper | `UserRowMapper` |
| `RETURN_GENERATED_KEYS` | `UserDao.insert` |
| 动态 SQL（结构拼接 + 参数绑定） | `UserDao.search` |
| `limit offset` 分页 + count | `UserDao.findPage` / `countByStatus` |
| 手动事务 `setAutoCommit(false)` | `UserDao.registerWithLog`、`AccountDao.transfer` |
| 事务隔离级别 | `AccountDao.transfer` → `READ_COMMITTED` |
| `addBatch` / `executeBatch` | `UserDao.batchInsert` |
| `rewriteBatchedStatements=true` | `application.yml` JDBC URL |

## 端口

| 服务 | 宿主机端口 |
| --- | --- |
| MySQL | **3316** |
| 应用 | **8189** |

## 一键启动（Docker）

```bash
cd JDBC
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 接口示例

```bash
# JDBC / 连接池
curl "http://localhost:8189/jdbc/hello"
curl "http://localhost:8189/jdbc/pool"

# 注册用户（手动事务：users + operation_log）
curl -X POST "http://localhost:8189/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 简单插入（单条 SQL + 回填主键）
curl -X POST "http://localhost:8189/users/simple" \
  -H "Content-Type: application/json" \
  -d '{"username":"钱七","email":"qianqi@example.com","age":30}'

# 批量插入（addBatch）
curl -X POST "http://localhost:8189/users/batch" \
  -H "Content-Type: application/json" \
  -d '[{"username":"u1","email":"u1@example.com","age":20},{"username":"u2","email":"u2@example.com","age":22}]'

curl "http://localhost:8189/users/1"
curl "http://localhost:8189/users/by-email?email=zhangsan@example.com"

curl -X POST "http://localhost:8189/users/search" \
  -H "Content-Type: application/json" \
  -d '{"status":"ACTIVE","minAge":18}'

curl "http://localhost:8189/users?status=ACTIVE&pageNumber=1&pageSize=10"

curl -X PUT "http://localhost:8189/users/1/email" \
  -H "Content-Type: application/json" \
  -d '{"email":"new-zhangsan@example.com"}'

curl -X DELETE "http://localhost:8189/users/4"

# 账户转账（手动 commit / rollback）
curl "http://localhost:8189/accounts"
curl -X POST "http://localhost:8189/accounts/transfer" \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":100.00}'

# 手写 JDBC join
curl "http://localhost:8189/orders/join?status=PAID"
```

## 本地开发

```bash
mvn spring-boot:run
```

默认连接 `localhost:3316/native_jdbc_demo`。

## 测试

```bash
mvn test
```

## 模块端口一览（仓库内）

| 模块 | MySQL | App |
| --- | --- | --- |
| MyBatisFlex | 3308 | 8180 |
| JdbcTemplate | 3309 | 8181 |
| MyBatisPlus | 3310 | 8182 |
| MyBatis | 3311 | 8183 |
| SpringDataJpa | 3312 | 8184 |
| Liquibase | 3313 | 8185 |
| Flyway | 3314 | 8186 |
| Tomcat | 3315 | 8187 |
| **JDBC** | **3316** | **8189** |
