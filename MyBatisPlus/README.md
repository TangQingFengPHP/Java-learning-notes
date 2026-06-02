# MyBatis-Plus 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- MyBatis-Plus（`BaseMapper` / `Wrapper` / `Page` / 逻辑删除 / 乐观锁 / 自动填充）
- MySQL 8.4
- HikariCP（Spring Boot 默认连接池）
- Lombok
- Docker / docker-compose

## 覆盖知识点（与文档对齐）

- `BaseMapper<T>`：`SysUserMapper` / `SysOrderMapper`
- `LambdaQueryWrapper`：动态条件查询、列表
- `LambdaUpdateWrapper`：条件更新（改邮箱、按年龄批量禁用）
- `Page<T>`：分页查询（`GET /users`）
- `@TableLogic`：逻辑删除（`deleted`）
- `@Version`：乐观锁（`version`）
- `MetaObjectHandler`：`createTime/updateTime` 自动填充
- `MybatisPlusInterceptor + PaginationInnerInterceptor`：分页插件配置

## 端口（避免与其它模块冲突）

- MySQL：`3310`（映射容器内 3306）
- 应用：`8182`

## 一键启动（Docker）

在 `MyBatisPlus/` 目录执行：

```bash
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 接口示例

```bash
# 新增用户（BaseMapper.save）
curl -X POST "http://localhost:8182/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 详情（逻辑删除后会查不到）
curl "http://localhost:8182/users/1"

# 动态条件查询（LambdaQueryWrapper）
curl -X POST "http://localhost:8182/users/search" \
  -H "Content-Type: application/json" \
  -d '{"username":"张","status":"ACTIVE","minAge":18}'

# 分页（Page + 分页插件）
curl "http://localhost:8182/users?status=ACTIVE&pageNumber=1&pageSize=10"

# 改邮箱（LambdaUpdateWrapper）
curl -X PUT "http://localhost:8182/users/1/email" \
  -H "Content-Type: application/json" \
  -d '{"email":"new-zhangsan@example.com"}'

# 条件更新：禁用年龄小于 18 的用户
curl -X PUT "http://localhost:8182/users/disable-by-age?ltAge=18"

# 乐观锁示例（@Version）
curl -X PUT "http://localhost:8182/users/1/optimistic-disable"

# 逻辑删除（@TableLogic）
curl -X DELETE "http://localhost:8182/users/1"

# 订单列表
curl "http://localhost:8182/orders?status=PAID"

# join（Wrapper/链式写法，MPJ）
curl "http://localhost:8182/orders/join/wrapper?status=PAID"

# join（XML 自定义 SQL）
curl "http://localhost:8182/orders/join/xml?status=PAID"
```

## 乱码/字符集

若插入中文出现乱码，通常是旧数据卷里已写入错误编码数据。可执行：

```bash
docker compose down -v
docker compose up --build
```

