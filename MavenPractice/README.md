# Maven 多模块实战

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Maven 3.9（多模块：继承 + 聚合）
- MySQL 8.4
- HikariCP（Spring Boot 默认连接池）
- Lombok
- Docker / docker-compose

## 模块结构

```text
MavenPractice/
├── pom.xml                 # 父工程（packaging=pom）：聚合 + dependencyManagement + pluginManagement
├── maven-common/           # 公共模型（ApiError、PageResult、枚举）
├── maven-domain/           # 实体与请求 DTO
├── maven-service/          # Repository + Service（JdbcTemplate）
└── maven-web/              # 启动模块（Controller + Spring Boot 可执行 jar）
```

## 覆盖 Maven 知识点

| 知识点 | 示例位置 |
| --- | --- |
| 坐标 `groupId/artifactId/version` | 各模块 `pom.xml` |
| `packaging=pom` 聚合工程 | 根 `pom.xml` `<modules>` |
| `<parent>` 继承 | 子模块继承父 POM |
| `dependencyManagement` | 父 POM 统一管理内部模块版本 |
| `pluginManagement` | 父 POM 统一 compiler / surefire / boot 插件 |
| 依赖 `scope`（runtime / test） | `mysql-connector-j`、`h2` |
| 资源过滤 `${project.version}` | `maven-web` 的 `application.yml`、`app-meta.properties` |
| Maven `profile`（dev / prod） | 根 `pom.xml` `<profiles>`，`-Pprod` 切换 |
| `spring-boot-maven-plugin:repackage` | `maven-web/pom.xml` |
| `build-info` | `GET /maven/info` 查看构建信息 |
| `mvn install` 本地仓库 | 根目录 `mvn clean install` 安装各子模块 |
| `dependency:tree` | `mvn dependency:tree -pl maven-web` |
| `help:effective-pom` | `mvn help:effective-pom -pl maven-web` |

## 端口说明

| 服务 | 宿主机端口 | 说明 |
| --- | --- | --- |
| MySQL | **3320** | 映射容器内 3306 |
| 应用 | **8193** | Spring Boot |

## 一键启动（Docker）

```bash
cd MavenPractice
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 本地 Maven 构建

```bash
cd MavenPractice

# 全量多模块构建并安装到本地仓库
mvn clean install

# 仅构建启动模块及其依赖
mvn clean package -pl maven-web -am

# 使用 Maven Wrapper（团队统一 Maven 3.9.9 版本）
./mvnw clean package -pl maven-web -am

# 查看依赖树
mvn dependency:tree -pl maven-web

# 使用 prod profile 打包（资源过滤 app.env=prod）
mvn clean package -pl maven-web -am -Pprod
```

运行可执行 jar：

```bash
java -jar maven-web/target/maven-web-1.0.0-SNAPSHOT.jar
```

开发模式：

```bash
mvn spring-boot:run -pl maven-web -am
```

## 接口示例

```bash
# Maven 构建信息（演示 build-info + 资源过滤）
curl "http://localhost:8193/maven/info"

# 注册
curl -X POST "http://localhost:8193/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'

# 详情
curl "http://localhost:8193/users/1"

# 分页
curl "http://localhost:8193/users?status=ACTIVE&pageNumber=1&pageSize=10"

# 动态搜索（NamedParameterJdbcTemplate）
curl -X POST "http://localhost:8193/users/search" \
  -H "Content-Type: application/json" \
  -d '{"username":"张","status":"ACTIVE","minAge":18}'

# 订单 join
curl "http://localhost:8193/orders/join"
```

## 本地开发（不用 Docker）

先确保 MySQL 已执行 `maven-web/src/main/resources/db/init.sql`，再：

```bash
mvn spring-boot:run -pl maven-web -am
```

默认连接：`localhost:3320/maven_demo`（可通过环境变量 `DB_URL` 覆盖）。
