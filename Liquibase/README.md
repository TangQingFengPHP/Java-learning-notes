# Liquibase 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Liquibase（数据库变更版本管理）
- Spring Data JPA + JdbcTemplate（业务读写，表结构交给 Liquibase）
- MySQL 8.4
- HikariCP
- Lombok
- Docker / docker-compose

## 与其他模块的关键区别

本模块 **不使用 `init.sql` 建表**。MySQL 容器只创建空库 `liquibase_demo`，表结构、索引、初始化数据均由 **Liquibase changelog** 在应用启动时自动执行。

```text
Liquibase 管表结构 / 索引 / 视图 / 初始化数据
JPA / JdbcTemplate 管业务读写
JPA ddl-auto: validate（不与 Liquibase 抢控制权）
```

## 覆盖知识点（与文档对齐）

| 知识点 | 示例位置 |
| --- | --- |
| master changelog + include | `db.changelog-master.yaml` |
| 版本目录拆分 | `v1.0.0` / `v1.1.0` / `v1.2.0` |
| YAML `createTable` / 索引 / 外键 | `001-create-users-table.yaml`、`002-create-orders-table.yaml` |
| `context` 环境隔离 | `003-init-user-data.yaml`（`dev,test`） |
| `addColumn` + `defaultValue` | `001-add-user-status-column.yaml` |
| `modifyDataType` | `002-modify-username-length.yaml` |
| `preConditions` + `onFail: MARK_RAN` | `003-create-config-table.yaml` |
| `labels` | `004-add-orders-status-index.yaml` |
| `rollback` | 各 changeSet 均含 rollback 定义 |
| XML 格式 changelog | `xml/001-create-product-table.xml` |
| formatted SQL + 视图 | `sql/001-create-report-view.sql` |
| `DATABASECHANGELOG` 追踪 | `GET /admin/changelog` |
| Maven Liquibase 插件 | `pom.xml`（profile `local-mysql`） |

## changelog 演进路线

```text
v1.0.0  创建 users / orders 表 + dev/test 种子数据
v1.1.0  users 增加 status 字段 + 回填 DISABLED
v1.2.0  增加 phone、扩 username 长度、app_config 表、orders.status 索引
XML     创建 products 表
SQL     创建 v_user_order_summary 视图（仅 MySQL）
```

## 端口

| 服务 | 宿主机端口 |
| --- | --- |
| MySQL | **3313** |
| 应用 | **8185** |

## 一键启动（Docker）

```bash
cd Liquibase
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

首次启动时 Liquibase 会自动创建 `DATABASECHANGELOG`、`DATABASECHANGELOGLOCK` 并执行全部未运行的 changeSet。

若需重建数据库：

```bash
docker compose down -v
docker compose up --build
```

## 接口示例

```bash
# 查看已执行的 changeSet 历史
curl "http://localhost:8185/admin/changelog"
curl "http://localhost:8185/admin/changelog/count"

# 用户 CRUD（表结构由 Liquibase 创建）
curl "http://localhost:8185/users"
curl "http://localhost:8185/users/1"
curl "http://localhost:8185/users/by-email?email=zhangsan@example.com"

curl -X POST "http://localhost:8185/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28,"phone":"13800138000"}'

# 订单 join 查询
curl "http://localhost:8185/orders/join?status=PAID"

# Liquibase SQL 创建的视图
curl "http://localhost:8185/reports/user-order-summary"

# preConditions 创建的 app_config 表
curl "http://localhost:8185/reports/config?key=app.name"

# XML changelog 创建的 products 表
curl -X POST "http://localhost:8185/reports/products?name=Java书&price=89.00"
curl "http://localhost:8185/reports/products"
```

## Maven Liquibase 命令（连接本地 MySQL）

```bash
cd Liquibase

# 查看待执行变更
mvn -Plocal-mysql liquibase:status

# 预览 SQL（不真正执行）
mvn -Plocal-mysql liquibase:updateSQL

# 执行迁移
mvn -Plocal-mysql liquibase:update

# 回滚最近 1 个 changeSet
mvn -Plocal-mysql liquibase:rollback -Dliquibase.rollbackCount=1
```

## context 说明

| 环境变量 | 含义 |
| --- | --- |
| `LIQUIBASE_CONTEXTS=dev,test` | 执行 dev/test 种子数据（Docker 默认） |
| `LIQUIBASE_CONTEXTS=prod` | 跳过带 `context: dev,test` 的 changeSet |

## 本地开发

```bash
mvn spring-boot:run
```

默认连接 `localhost:3313/liquibase_demo`。

## 测试

```bash
mvn test
```

`SmokeTest` 使用 H2 + Liquibase 自动迁移（MySQL 专用视图 changeSet 通过 `dbms:mysql` 跳过）。
