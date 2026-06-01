# MyBatis-Flex 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- MyBatis-Flex（`BaseMapper` / `QueryWrapper` / APT 表定义类）
- MySQL 8.4
- HikariCP（Spring Boot 默认连接池）
- Lombok
- Docker / docker-compose

## 目录结构

```text
MyBatisFlex
├── docker-compose.yml
├── Dockerfile
├── mybatis-flex.config
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com/github/mybatisflex
    │   │       ├── controller
    │   │       ├── entity
    │   │       ├── mapper
    │   │       ├── model
    │   │       └── service
    │   └── resources
    │       ├── application.yml
    │       ├── db/init.sql
    │       └── mapper/OrderMapper.xml
    └── test
        └── java
```

## 一键启动（Docker）

在 `MyBatisFlex/` 目录执行：

```bash
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

`Dockerfile` 使用 BuildKit 依赖缓存 + `pom.xml` 分层；Maven 走 `docker/maven-settings.xml`（阿里云镜像）。仅改 `src` 时重复构建会快很多。

启动后：

- 应用：`http://localhost:8080`
- MySQL：`localhost:3306`（root / 123456）

## 接口示例

### User（BaseMapper + QueryWrapper + 分页 + 逻辑删除 + 乐观锁）

- 新增用户

```bash
curl -X POST "http://localhost:8080/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'
```

- 用户详情（逻辑删除后会查不到）

```bash
curl "http://localhost:8080/users/1"
```

- 动态条件查询（`QueryWrapper` 组装 where）

```bash
curl -X POST "http://localhost:8080/users/search" \
  -H "Content-Type: application/json" \
  -d '{"username":"张","status":"ACTIVE","minAge":18}'
```

- 分页查询（`paginate`）

```bash
curl "http://localhost:8080/users?status=ACTIVE&pageNumber=1&pageSize=10"
```

- 更新邮箱（简单 update）

```bash
curl -X PUT "http://localhost:8080/users/1/email" \
  -H "Content-Type: application/json" \
  -d '{"email":"new-zhangsan@example.com"}'
```

- 条件更新：禁用所有年龄小于 18 的用户（`updateByQuery`）

```bash
curl -X PUT "http://localhost:8080/users/disable-by-age?ltAge=18"
```

- 乐观锁示例：禁用单用户（`@Column(version=true)`）

```bash
curl -X PUT "http://localhost:8080/users/1/optimistic-disable"
```

- 删除用户（逻辑删除）

```bash
curl -X DELETE "http://localhost:8080/users/1"
```

### Order（join DTO：QueryWrapper vs XML，Db+Row）

- join（QueryWrapper + `selectListByQueryAs`）

```bash
curl "http://localhost:8080/orders/join/wrapper?status=PAID"
```

- join（XML 自定义 SQL）

```bash
curl "http://localhost:8080/orders/join/xml?status=PAID"
```

- Db+Row（直接 SQL）

```bash
curl "http://localhost:8080/orders/rows?status=PAID"
```

## APT 表定义类说明

`QueryWrapper` 示例里使用了：

```text
USER / ORDER
```

这些来自编译期 APT 生成的表定义类（默认在 `target/generated-sources/annotations`）。

