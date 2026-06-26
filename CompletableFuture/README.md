# Java Future 与 CompletableFuture 实战模块

## 技术栈

- JDK 21（`orTimeout`、`failedFuture`、虚拟线程执行器）
- Spring Boot 3.4.x
- Spring MVC（Controller 直接返回 `CompletableFuture`）
- Spring JDBC + JdbcTemplate
- MySQL 8.4 + HikariCP
- Lombok
- Java HttpClient（模拟远程服务调用）
- Docker / docker-compose

## 覆盖知识点

| 知识点 | 示例位置 |
| --- | --- |
| `ExecutorService.submit` + `Future.get()` | `GET /api/future/basic` |
| `Future.get(timeout)` + `cancel(true)` | `GET /api/future/timeout` |
| 自定义有界线程池 | `AsyncConfig.queryExecutor` |
| `supplyAsync` + 指定 Executor | 各 Service 层 |
| `thenApply` / `thenCompose` 链式编排 | `GET /api/demo/chain` |
| `runAsync` / `thenRun` 无返回值任务 | `GET /api/demo/fire-and-forget` |
| `allOf` 并行等待 | `GET /api/products/batch?ids=1,2,3` |
| `anyOf` / `applyToEither` 竞速读取 | `GET /api/config/fastest-anyof` |
| `exceptionally` / `handle` 异常兜底 | `GET /api/demo/exception` |
| `whenComplete` 打点观察（不改变结果） | `GET /api/demo/when-complete` |
| 手动 `complete` / `completeExceptionally` | `GET /api/demo/manual-complete` |
| 老式回调 API 桥接为 CF | `GET /api/demo/legacy-bridge` |
| `completeOnTimeout` + `orTimeout` | `GET /api/users/{id}/home` |
| 用户首页聚合（并行查库 + 远程） | `UserHomeService` |
| 下单流程（串行 + 并行 + 串行） | `POST /api/orders` |
| Spring `@Async` 返回 `CompletableFuture` | `POST /api/async/orders` |
| 虚拟线程 + `CompletableFuture` | `GET /api/demo/virtual-thread` |
| 模拟慢接口 | `/mock/**` |

## 端口说明

| 服务 | 宿主机端口 | 说明 |
| --- | --- | --- |
| MySQL | **3319** | 映射容器内 3306 |
| 应用 | **8192** | Spring Boot |

## 一键启动（Docker）

```bash
cd CompletableFuture
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 接口示例

```bash
# Future 基础：submit + get
curl "http://localhost:8192/api/future/basic"

# Future 超时与取消
curl "http://localhost:8192/api/future/timeout?taskMs=3000&timeoutMs=1000"

# CompletableFuture 用户首页聚合（allOf + completeOnTimeout + orTimeout）
curl "http://localhost:8192/api/users/1/home"

# CompletableFuture 下单编排（thenCompose + allOf）
curl -X POST "http://localhost:8192/api/orders" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":1,"quantity":1}'

# allOf 批量查商品
curl "http://localhost:8192/api/products/batch?ids=1,2,3"

# anyOf 竞速读配置
curl "http://localhost:8192/api/config/fastest-anyof"

# applyToEither 主备竞速
curl "http://localhost:8192/api/config/fastest-either"

# thenApply + thenCompose 链式演示
curl "http://localhost:8192/api/demo/chain?userId=1"

# exceptionally 异常兜底
curl "http://localhost:8192/api/demo/exception?fail=true&mode=exceptionally"

# handle 统一处理成功与失败
curl "http://localhost:8192/api/demo/exception?fail=true&mode=handle"

# whenComplete 打点观察（成功 / 失败都会记录 metrics）
curl "http://localhost:8192/api/demo/when-complete"
curl "http://localhost:8192/api/demo/when-complete?fail=true"

# 手动 complete：先创建 Future，再由其他线程填入结果
curl "http://localhost:8192/api/demo/manual-complete?delayMs=500"

# 老式回调 API 桥接为 CompletableFuture
curl "http://localhost:8192/api/demo/legacy-bridge?userId=1"
curl "http://localhost:8192/api/demo/legacy-bridge?userId=1&fail=true"

# 虚拟线程执行器 + supplyAsync
curl "http://localhost:8192/api/demo/virtual-thread"

# Spring @Async 返回 CompletableFuture
curl -X POST "http://localhost:8192/api/async/orders?userId=1001&productId=2001"
```

## 与 VirtualThread 模块的区别

| 对比项 | 本模块（CompletableFuture） | VirtualThread 模块 |
| --- | --- | --- |
| 核心能力 | 异步结果编排、组合、兜底 | 虚拟线程降低阻塞成本 |
| 并发模型 | `CompletableFuture` 流水线 | `Future` + 虚拟线程执行器 |
| 典型场景 | 多服务聚合、任务依赖编排 | 大量阻塞 I/O 并发 |

二者可组合使用：I/O 密集任务用虚拟线程执行器承载，`CompletableFuture` 负责表达依赖关系。

## 本地开发（不用 Docker）

先确保 MySQL 已执行 `src/main/resources/db/init.sql`，再：

```bash
mvn spring-boot:run
```

默认连接：`localhost:3319/completable_future_demo`。

## 测试

```bash
mvn test
```

- `SmokeTest`：H2 内存库启动上下文
- `CompletableFutureApiTest`：纯 JDK API 单元测试（thenApply、allOf、anyOf、超时、异常兜底、whenComplete、手动 complete）
