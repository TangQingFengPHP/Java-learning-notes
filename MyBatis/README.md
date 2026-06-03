# MyBatis 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- MyBatis（原生 XML 映射 + 动态 SQL）
- MySQL 8.4
- HikariCP（Spring Boot 默认连接池）
- Lombok
- Docker / docker-compose

## 覆盖知识点（与文档对齐）

| 知识点 | 示例位置 |
| --- | --- |
| Mapper 接口 + XML | `SysUserMapper` / `SysUserMapper.xml` |
| `resultMap` / `resultType` | `UserResultMap`、订单 join DTO |
| `<sql>` 片段复用 | `<sql id="UserColumns">` + `<include>` |
| 动态 SQL `<where>` + `<if>` | `search`、`selectPage` |
| `<foreach>` IN 查询 | `selectByIds` |
| `<set>` 动态更新 | `update` |
| `useGeneratedKeys` 回填主键 | `insert` |
| `limit offset` 手写分页 | `selectPage` + `countByStatus` |
| **PageHelper 分页插件** | `pageByHelper` / `searchPageByHelper` |
| 多表 join + DTO | `SysOrderMapper.xml` |
| 逻辑删除（XML 实现） | `logicDeleteById` |
| `@Param` 多参数 | Mapper 接口方法 |
| `#{}` 参数绑定 | 全部 XML SQL |

## 端口（避免与其它模块冲突）

| 服务 | 宿主机端口 |
| --- | --- |
| MySQL | **3311** |
| 应用 | **8183** |

## 一键启动（Docker）

```bash
cd MyBatis
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 接口示例

```bash
# 新增（XML insert + useGeneratedKeys）
curl -X POST "http://localhost:8183/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 详情（resultMap）
curl "http://localhost:8183/users/1"

# 动态条件查询（where + if）
curl -X POST "http://localhost:8183/users/search" \
  -H "Content-Type: application/json" \
  -d '{"keyword":"张","status":"ACTIVE","minAge":18}'

# 分页（limit offset + count，手写）
curl "http://localhost:8183/users?status=ACTIVE&pageNumber=1&pageSize=10"

# 分页（PageHelper 插件，SQL 无需 limit offset）
curl "http://localhost:8183/users/page-helper?status=ACTIVE&pageNumber=1&pageSize=10"

# 动态条件 + PageHelper 分页
curl -X POST "http://localhost:8183/users/search-page?pageNumber=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -d '{"keyword":"张","status":"ACTIVE","minAge":18}'

# IN 查询（foreach）
curl "http://localhost:8183/users/by-ids?ids=1,2,3"

# 动态更新邮箱（set + if）
curl -X PUT "http://localhost:8183/users/1/email" \
  -H "Content-Type: application/json" \
  -d '{"email":"new-zhangsan@example.com"}'

# 条件批量禁用
curl -X PUT "http://localhost:8183/users/disable-by-age?ltAge=18"

# 逻辑删除
curl -X DELETE "http://localhost:8183/users/1"

# 订单 join（XML 多表查询）
curl "http://localhost:8183/orders/join/xml?status=PAID"
```

## 乱码/字符集

若中文乱码，清空旧数据卷后重建：

```bash
docker compose down -v
docker compose up --build
```
