# Spring Data JPA 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Spring Data JPA（Hibernate 实现）
- MySQL 8.4
- HikariCP
- Lombok
- Docker / docker-compose

## 覆盖知识点（与文档对齐）

| 知识点 | 示例位置 |
| --- | --- |
| `@Entity` / `@Table` / `@Column` | `User`、`Order` |
| `@OneToMany` / `@ManyToOne` 关联 | `User.orders`、`Order.user` |
| `JpaRepository` CRUD | `UserRepository` |
| 方法名查询 | `findByEmail`、`findByStatusOrderByIdDesc` |
| `Pageable` + `Page` 分页 | `GET /users`、`POST /users/search` |
| `Slice`（不查总数） | `GET /users/slice` |
| `@Query` JPQL | `findActiveUsers` |
| `@Query` 原生 SQL | `findByNativeSql` |
| `@Modifying` 更新 | `updateStatus`、`disableByAgeLessThan` |
| `Specification` 动态条件 | `UserSpecifications` |
| 接口投影 Projection | `GET /users/summary` |
| JPQL 构造 DTO | `GET /users/summary-dto` |
| `@EntityGraph` 避免 N+1 | `GET /users/{id}/with-orders` |
| `@Version` 乐观锁 | `PUT /users/{id}/optimistic-disable` |
| `@EnableJpaAuditing` | `BaseAuditableEntity` |
| JPQL join DTO | `GET /orders/join` |
| `open-in-view: false` | `application.yml` |
| `ddl-auto: validate` | 表结构由 `init.sql` 管理 |

## 端口（避免与其它模块冲突）

| 服务 | 宿主机端口 |
| --- | --- |
| MySQL | **3312** |
| 应用 | **8184** |

## 一键启动（Docker）

```bash
cd SpringDataJpa
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

若曾用旧数据卷导致乱码或表结构不一致，先执行：

```bash
docker compose down -v
```

## 接口示例

```bash
# 新增（JpaRepository.save + 审计字段）
curl -X POST "http://localhost:8184/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 详情
curl "http://localhost:8184/users/1"

# EntityGraph 加载订单（避免 N+1）
curl "http://localhost:8184/users/1/with-orders"

# 方法名查询
curl "http://localhost:8184/users/by-email?email=zhangsan@example.com"

# Specification 动态分页
curl -X POST "http://localhost:8184/users/search?pageNumber=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -d '{"keyword":"张","status":"ACTIVE","minAge":18}'

# 简单状态分页（Page，含总数）
curl "http://localhost:8184/users?status=ACTIVE&pageNumber=1&pageSize=10"

# Slice 分页（不查 count）
curl "http://localhost:8184/users/slice?status=ACTIVE&pageNumber=1&pageSize=10"

# JPQL / 原生 SQL
curl "http://localhost:8184/users/jpql?status=ACTIVE&minAge=18"
curl "http://localhost:8184/users/native?status=ACTIVE&minAge=18"

# 接口投影 / JPQL DTO
curl "http://localhost:8184/users/summary?status=ACTIVE"
curl "http://localhost:8184/users/summary-dto?status=ACTIVE"

# IN 查询
curl "http://localhost:8184/users/by-ids?ids=1,2,3"

# 脏检查更新邮箱（事务内改实体属性）
curl -X PUT "http://localhost:8184/users/1/email" \
  -H "Content-Type: application/json" \
  -d '{"email":"new-zhangsan@example.com"}'

# @Modifying 批量禁用
curl -X PUT "http://localhost:8184/users/disable-by-age?ltAge=18"

# 乐观锁禁用
curl -X PUT "http://localhost:8184/users/1/optimistic-disable"

# 删除
curl -X DELETE "http://localhost:8184/users/4"

# 订单 join DTO（JPQL fetch join 构造）
curl "http://localhost:8184/orders/join?status=PAID"
```

## 本地开发（不用 Docker）

```bash
cd SpringDataJpa
mvn spring-boot:run
```

默认连接：`localhost:3312/jpa_demo`（可用环境变量 `DB_URL` 覆盖）。

## 测试

```bash
mvn test
```

`SmokeTest` 使用 H2 内存库 + `ddl-auto=create-drop`，无需 MySQL。
