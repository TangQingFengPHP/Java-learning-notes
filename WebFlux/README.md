# Spring WebFlux 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Spring WebFlux（Reactor Netty）
- Spring Data R2DBC + MySQL（响应式数据访问）
- R2DBC Connection Pool（非阻塞连接池，WebFlux 场景替代 JDBC 的 HikariCP）
- WebClient（响应式 HTTP 客户端）
- Lombok
- Docker / docker-compose

> 说明：WebFlux 全链路非阻塞应使用 R2DBC，而不是 JDBC + HikariCP。若核心链路大量 JDBC/JPA，更适合 Spring MVC。

## 覆盖知识点

| 知识点 | 示例位置 |
| --- | --- |
| `Mono` / `Flux` 返回值 | `UserController`、`UserService` |
| R2DBC `ReactiveCrudRepository` | `UserRepository` |
| `map` / `flatMap` / `switchIfEmpty` | `UserService.create`、`findById` |
| 注解式 Controller | `GET/POST /api/users/**` |
| 函数式路由 `RouterFunction` | `GET/POST /fn/users/**` |
| SSE 流式推送 | `GET /api/users/stream`、`/api/users/events` |
| `WebClient` + `onStatus` + `timeout` | `RemoteUserClient` |
| `Mono.zip` 并发聚合 | `GET /api/users/{id}/detail` |
| `@RestControllerAdvice` 全局异常 | `GlobalExceptionHandler` |
| `WebTestClient` | `UserControllerTest` |
| `StepVerifier` | `ReactorTest` |

## 端口说明

| 服务 | 宿主机端口 | 说明 |
| --- | --- | --- |
| MySQL | **3317** | 映射容器内 3306 |
| 应用 | **8190** | Spring WebFlux |

## 一键启动（Docker）

```bash
cd WebFlux
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 接口示例

```bash
# 创建用户
curl -X POST "http://localhost:8190/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 查询单个用户
curl "http://localhost:8190/api/users/1"

# 查询列表
curl "http://localhost:8190/api/users"

# 条件分页
curl "http://localhost:8190/api/users/search?status=ACTIVE&keyword=张&page=1&size=10"

# 禁用用户
curl -X PUT "http://localhost:8190/api/users/1/disable"

# 删除用户
curl -X DELETE "http://localhost:8190/api/users/1"

# SSE 流式推送用户列表（每秒一条）
curl -N "http://localhost:8190/api/users/stream"

# SSE 演示事件流
curl -N "http://localhost:8190/api/users/events"

# Mono.zip 聚合：本地用户 + WebClient 远程快照 + 活跃用户数
curl "http://localhost:8190/api/users/1/detail"

# 函数式路由
curl "http://localhost:8190/fn/users/1"
curl -X POST "http://localhost:8190/fn/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"钱七","email":"qianqi@example.com","age":30}'
```

## 本地开发（不用 Docker）

先确保 MySQL 已执行 `src/main/resources/db/init.sql`，再：

```bash
mvn spring-boot:run
```

默认连接：`localhost:3317/webflux_demo`（可通过环境变量 `R2DBC_URL` 覆盖）。

## 中文乱码说明

若 `username` 显示乱码，请清空旧数据卷后重建：

```bash
docker compose down -v
docker compose up --build
```

Navicat 连接请使用 **UTF-8 / utf8mb4** 字符集。

## 测试

```bash
mvn test
```

- `SmokeTest`：H2 + R2DBC 内存库启动上下文
- `UserControllerTest`：`WebTestClient` 测试 Controller
- `ReactorTest`：`StepVerifier` 测试 `Flux` 操作符
