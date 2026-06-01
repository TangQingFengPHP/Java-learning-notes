# JdbcTemplate 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Spring JDBC（`JdbcTemplate` / `NamedParameterJdbcTemplate`）
- MySQL 8.4
- HikariCP（Spring Boot 默认连接池）
- Lombok
- Docker / docker-compose

## 覆盖知识点

| 知识点 | 示例位置 |
| --- | --- |
| `JdbcTemplate.update` | 新增 / 改邮箱 / 删除 |
| `queryForObject` | `GET /users/count`、`GET /users/{id}/email` |
| `query` + `RowMapper` | 列表、详情、订单 join |
| `Optional` 查不到 | `UserRepository.findById` |
| `GeneratedKeyHolder` | 注册返回自增 ID |
| `batchUpdate` | `PUT /users/batch-status` |
| `NamedParameterJdbcTemplate` | `POST /users/search`、订单 join |
| `IN (:ids)` | `GET /users/by-ids` |
| `BeanPropertyRowMapper` | `GET /users/bean-mapper` |
| `limit offset` 分页 | `GET /users`、`GET /users/page-jdbc` |
| `@Transactional` | 注册 + 写注册日志 |

## 端口说明（与 MyBatisFlex 并存）

| 服务 | 宿主机端口 | 说明 |
| --- | --- | --- |
| MySQL | **3309** | 映射容器内 3306 |
| 应用 | **8181** | Spring Boot |

## 一键启动（Docker）

```bash
cd JdbcTemplate
# 建议开启 BuildKit（依赖缓存 + 分层构建）
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

### Docker 构建加速说明

`Dockerfile` 已做优化：

- **依赖分层**：先只 `COPY pom.xml` 并 `dependency:go-offline`，改 Java 代码时通常不必重新下载依赖
- **BuildKit 缓存**：`--mount=type=cache` 持久化 `/root/.m2/repository`，重复 `build` 明显更快
- **阿里云镜像**：`docker/maven-settings.xml` 加速国内下载
- **`.dockerignore`**：排除 `target/` 等，减少构建上下文

仅改业务代码后二次构建，一般只需 **编译 + 打 jar**（几十秒级），不必再等全量拉依赖。

## 接口示例

```bash
# 注册（事务：用户 + 注册日志）
curl -X POST "http://localhost:8181/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 详情
curl "http://localhost:8181/users/1"

# queryForObject 单字段
curl "http://localhost:8181/users/1/email"

# 统计
curl "http://localhost:8181/users/count"

# NamedParameter 动态条件
curl -X POST "http://localhost:8181/users/search" \
  -H "Content-Type: application/json" \
  -d '{"username":"张","status":"ACTIVE","minAge":18}'

# 分页
curl "http://localhost:8181/users?status=ACTIVE&pageNumber=1&pageSize=10"

# IN 查询
curl "http://localhost:8181/users/by-ids?ids=1,2,3"

# BeanPropertyRowMapper
curl "http://localhost:8181/users/bean-mapper"

# 批量更新状态
curl -X PUT "http://localhost:8181/users/batch-status" \
  -H "Content-Type: application/json" \
  -d '{"ids":[2,3],"status":"DISABLED"}'

# 订单 join（NamedParameterJdbcTemplate）
curl "http://localhost:8181/orders/join?status=PAID"
```

## 中文乱码说明

若 `username` 显示为 `å¼ ä¸‰` 这类字符，说明数据按错误编码写入了。请：

1. 使用修复后的 JDBC URL（`UTF-8` + `utf8mb4`）
2. **清空旧数据卷后重建**（否则 init 脚本里的脏数据仍在）：

```bash
docker compose down -v
docker compose up --build
```

Navicat 连接请使用 **UTF-8 / utf8mb4** 字符集。

## 本地开发（不用 Docker）

先确保 MySQL 已执行 `src/main/resources/db/init.sql`，再：

```bash
mvn spring-boot:run
```

默认连接：`localhost:3309/jdbc_demo`（可在环境变量 `DB_URL` 覆盖）。
