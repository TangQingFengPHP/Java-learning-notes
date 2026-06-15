# Java 虚拟线程实战模块

## 技术栈

- JDK 21（虚拟线程正式特性）
- Spring Boot 3.4.x
- Spring MVC（同步阻塞写法 + 虚拟线程承载请求）
- Spring JDBC + JdbcTemplate
- MySQL 8.4 + HikariCP
- Java HttpClient（同步 `send` + 虚拟线程并发聚合）
- Lombok
- Docker / docker-compose

## 覆盖知识点

| 知识点 | 示例位置 |
| --- | --- |
| `spring.threads.virtual.enabled=true` | `application.yml` |
| 请求由虚拟线程处理 | `GET /api/users/{id}` 返回 `virtual: true` |
| `Executors.newVirtualThreadPerTaskExecutor()` | `ExecutorConfig`、`UserDetailService` |
| `Future` 并发聚合 | `GET /api/users/{id}/detail` |
| Java HttpClient 同步调用 | `HttpAggregationClient` |
| `Semaphore` 限制下游并发 | `LimitedRemoteClient`、`GET /api/concurrent/limited-remote` |
| HikariCP 连接池与虚拟线程 | `application.yml`、`GET /api/concurrent/batch-query` |
| `ThreadLocal` 链路追踪 | `TraceContext`、`TraceIdFilter` |
| `@Async` 异步任务 | `ReportService`、`POST /api/reports/{id}/generate` |
| CPU 有界平台线程池 | `ExecutorConfig.cpuBoundExecutor` |
| 模拟慢接口 | `/mock/**` |

## 端口说明

| 服务 | 宿主机端口 | 说明 |
| --- | --- | --- |
| MySQL | **3318** | 映射容器内 3306 |
| 应用 | **8191** | Spring Boot |

## 一键启动（Docker）

```bash
cd VirtualThread
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 接口示例

```bash
# 查询用户（响应中包含当前虚拟线程信息）
curl "http://localhost:8191/api/users/1"

# 查看当前线程信息
curl "http://localhost:8191/api/thread/current"

# 虚拟线程 + HttpClient 并发聚合（用户 + 订单 + 账户 + 活跃用户数）
curl "http://localhost:8191/api/users/1/detail"

# 大量虚拟线程并发 JDBC 查询（受 HikariCP 连接池限制）
curl "http://localhost:8191/api/concurrent/batch-query?count=200"

# Semaphore 限制远程并发（默认最多 20 个同时访问下游）
curl "http://localhost:8191/api/concurrent/limited-remote?count=50"

# @Async 异步报表（任务在虚拟线程中执行）
curl -X POST "http://localhost:8191/api/reports/1001/generate"

# 创建用户
curl -X POST "http://localhost:8191/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 条件分页
curl "http://localhost:8191/api/users/search?status=ACTIVE&keyword=张&page=1&size=10"
```

## 虚拟线程 vs WebFlux

| 对比项 | 虚拟线程（本模块） | WebFlux |
| --- | --- | --- |
| 编程风格 | 同步阻塞 | 响应式 `Mono`/`Flux` |
| JDBC | 可直接使用 | 需 R2DBC 或隔离 |
| 学习成本 | 低，沿用 MVC 写法 | 需熟悉 Reactor |

## 本地开发（不用 Docker）

先确保 MySQL 已执行 `src/main/resources/db/init.sql`，再：

```bash
mvn spring-boot:run
```

默认连接：`localhost:3318/virtual_thread_demo`。

## 排查建议

JDK 21–23 可用 `-Djdk.tracePinnedThreads=full` 观察 `synchronized` 导致的载体线程固定；JDK 24+ 已大幅改善该问题。

```bash
java -Djdk.tracePinnedThreads=full -jar app.jar
```

## 测试

```bash
mvn test
```

- `SmokeTest`：H2 内存库启动上下文
- `VirtualThreadApiTest`：`Thread.startVirtualThread`、`newVirtualThreadPerTaskExecutor`
- `ThreadInfoControllerTest`：MockMvc 验证线程信息接口
