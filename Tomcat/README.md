# Tomcat 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x（内嵌 **Tomcat 10.1**，Jakarta Servlet）
- Spring Data JPA + JdbcTemplate
- MySQL 8.4 + HikariCP
- Lombok
- Docker / docker-compose

## 覆盖知识点（与文档对齐）

| 知识点 | 示例位置 |
| --- | --- |
| 内嵌 Tomcat（JAR/WAR 可执行） | `Dockerfile` + `docker-compose.yml` |
| 外部 Tomcat 10.1 WAR 部署 | `Dockerfile.external` + `docker-compose.external.yml` |
| `SpringBootServletInitializer` | `TomcatPracticeApplication` |
| `@WebServlet` 原生 Servlet | `HelloServlet` → `/servlet/hello` |
| Servlet `Filter` | `RequestLoggingFilter` |
| 内嵌 Tomcat 线程池配置 | `application.yml` → `server.tomcat.*` |
| 反向代理头识别 | `forward-headers-strategy: framework` |
| 访问日志 | `server.tomcat.accesslog` |
| Connector 信息查询 | `GET /tomcat/info` |
| Nginx 反向代理示例 | `docker/nginx/default.conf` |

## 两种部署方式

```text
方式一（默认）：Spring Boot 内嵌 Tomcat，java -jar app.war
方式二：外部 Tomcat 10.1 部署 WAR（context-path = /app）
```

| 方式 | 命令 | 访问前缀 |
| --- | --- | --- |
| 内嵌 Tomcat | `docker compose up --build` | `http://localhost:8187` |
| 外部 Tomcat | `docker compose -f docker-compose.external.yml up --build` | `http://localhost:8188/app` |

## 端口

| 服务 | 宿主机端口 |
| --- | --- |
| MySQL | **3315** |
| 内嵌 Tomcat 应用 | **8187** |
| 外部 Tomcat | **8188**（映射容器 8080） |

## 一键启动（内嵌 Tomcat）

```bash
cd Tomcat
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 外部 Tomcat WAR 部署

```bash
cd Tomcat
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose -f docker-compose.external.yml up --build
```

Maven 本地打外部 Tomcat 用 WAR：

```bash
mvn -Pexternal-tomcat clean package -DskipTests
# 产物：target/tomcat-practice.war → 复制到 Tomcat webapps/app.war
```

## 接口示例（内嵌模式，端口 8187）

```bash
# Spring MVC
curl "http://localhost:8187/tomcat/hello"
curl "http://localhost:8187/tomcat/info"

# 原生 Servlet（jakarta.servlet）
curl "http://localhost:8187/servlet/hello"

# 业务 API
curl "http://localhost:8187/users"
curl "http://localhost:8187/users/1"
curl "http://localhost:8187/orders/join?status=PAID"

curl -X POST "http://localhost:8187/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"赵六","email":"zhaoliu@example.com","age":28}'
```

## 接口示例（外部 Tomcat，端口 8188，context-path `/app`）

```bash
curl "http://localhost:8188/app/tomcat/hello"
curl "http://localhost:8188/app/servlet/hello"
curl "http://localhost:8188/app/users"
curl "http://localhost:8188/app/tomcat/info"
```

## Tomcat 版本说明

| Tomcat | Servlet API | 适用 |
| --- | --- | --- |
| 9.x | `javax.servlet.*` | Spring Boot 2 |
| **10.1.x** | **`jakarta.servlet.*`** | **Spring Boot 3（本项目）** |
| 11.x | `jakarta.servlet.*` | Java 17+ 新项目 |

本项目使用 **Tomcat 10.1 + Jakarta EE 10**，与 Spring Boot 3.4 默认内嵌版本一致。

## 内嵌 Tomcat 配置（application.yml）

```yaml
server:
  tomcat:
    threads:
      max: 200
      min-spare: 10
    accept-count: 100
    connection-timeout: 20s
  forward-headers-strategy: framework
```

## 本地开发

```bash
mvn spring-boot:run
# 或
java -jar target/tomcat-practice.war
```

## 测试

```bash
mvn test
```

## Nginx 反向代理（可选）

参考 `docker/nginx/default.conf`，将请求转发到内嵌 Tomcat 8187 端口，配合 `forward-headers-strategy: framework` 识别 `X-Forwarded-*` 头。
