# Flyway 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Flyway（SQL 脚本管理数据库版本）
- Spring Data JPA + JdbcTemplate（业务读写，表结构交给 Flyway）
- MySQL 8.4
- HikariCP
- Lombok
- Docker / docker-compose

## 与其他模块的关键区别

本模块 **不使用 `init.sql` 建表**。MySQL 容器只创建空库 `flyway_demo`，表结构由 **Flyway 迁移脚本** 在应用启动时自动执行。

```text
Flyway 管表结构 / 索引 / 视图 / 初始化数据
JPA / JdbcTemplate 管业务读写
JPA ddl-auto: validate
```

与 `Liquibase/` 模块对比：Flyway 以 **纯 SQL 文件 + 版本号** 为主，规则更简单直接。

## 覆盖知识点（与文档对齐）

| 知识点 | 示例位置 |
| --- | --- |
| `V` 版本迁移命名 | `V1__create_users_table.sql` |
| `R` 重复迁移（视图） | `R__create_user_order_summary_view.sql` |
| 多环境脚本目录 | `db/migration` + `db/dev` |
| Java Migration | `V9__normalize_user_email.java` |
| `flyway_schema_history` | `GET /admin/migrations` |
| `validate-on-migrate` | `application.yml` |
| `clean-disabled: true` | 生产禁用 clean |
| `out-of-order: false` | 禁止乱序执行 |
| Maven Flyway 插件 | `pom.xml`（profile `local-mysql`） |

## 迁移脚本演进

```text
V1  创建 users 表
V2  创建 orders 表（索引 + 外键）
V3  创建 app_config 表 + 生产配置
V4  users 增加 status 字段
V5  users 增加 phone 字段
V6  username 扩长到 varchar(100)
V7  创建 products 表
V8  orders.status 索引
V9  Java Migration：邮箱转小写
R   可重复刷新 v_user_order_summary 视图
V1000（db/dev）开发测试种子数据（生产不加载）
```

## 端口

| 服务 | 宿主机端口 |
| --- | --- |
| MySQL | **3314** |
| 应用 | **8186** |

## 一键启动（Docker）

```bash
cd Flyway
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

重建数据库：

```bash
docker compose down -v
docker compose up --build
```

## 接口示例

```bash
# 查看 flyway_schema_history 迁移历史
curl "http://localhost:8186/admin/migrations"
curl "http://localhost:8186/admin/migrations/count"

# 用户
curl "http://localhost:8186/users"
curl "http://localhost:8186/users/1"
curl "http://localhost:8186/users/by-email?email=zhangsan@example.com"

curl -X POST "http://localhost:8186/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28,"phone":"13800138000"}'

# 订单 join
curl "http://localhost:8186/orders/join?status=PAID"

# R 脚本创建的视图
curl "http://localhost:8186/reports/user-order-summary"

# V3 创建的配置表
curl "http://localhost:8186/reports/config?key=app.name"

# V7 创建的 products 表
curl -X POST "http://localhost:8186/reports/products?name=Java书&price=89.00"
curl "http://localhost:8186/reports/products"
```

## 多环境脚本目录

| 环境变量 | 含义 |
| --- | --- |
| `FLYWAY_LOCATIONS=classpath:db/migration,classpath:db/dev` | 执行结构迁移 + 开发种子数据（Docker 默认） |
| `FLYWAY_LOCATIONS=classpath:db/migration` | 仅结构迁移，跳过 `V1000` 测试数据（生产推荐） |

## Maven Flyway 命令

```bash
cd Flyway

mvn -Plocal-mysql flyway:info
mvn -Plocal-mysql flyway:validate
mvn -Plocal-mysql flyway:migrate
mvn -Plocal-mysql flyway:repair
```

## 本地开发

```bash
mvn spring-boot:run
```

默认连接 `localhost:3314/flyway_demo`。

## 测试

```bash
mvn test
```

`SmokeTest` 使用 H2 + Flyway 自动迁移（含 `db/dev` 种子数据）。

## 注意事项

- **已执行的 `V` 脚本不要修改**，checksum 变化会导致 `validate` 失败；应新增 `V10__...sql` 修正
- 生产环境设置 `FLYWAY_LOCATIONS=classpath:db/migration`，避免测试数据进入生产库
- `clean` 已在配置中禁用（`clean-disabled: true`）
