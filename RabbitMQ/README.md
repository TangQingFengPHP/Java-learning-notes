# RabbitMQ 实战模块

## 技术栈

- JDK 21
- Spring Boot 3.4.x
- Spring AMQP（RabbitTemplate + @RabbitListener）
- RabbitMQ 3.13（management 镜像）
- MySQL 8.4
- HikariCP
- Lombok
- Docker / docker-compose

## 覆盖知识点

| 知识点 | 示例位置 |
| --- | --- |
| Direct Exchange | `OrderRabbitConfig` + `POST /orders` |
| Fanout Exchange | `NoticeRabbitConfig` + `POST /users/register` |
| Topic Exchange | `BusinessEventRabbitConfig` + `POST /events/business` |
| JSON 消息转换 | `RabbitMessageConfig` |
| 发布确认 publisher-confirm | `application.yml` + `RabbitTemplateCallbackConfig` |
| 路由失败 returns | `POST /demo/invalid-route` |
| 手动 ACK | 各 Consumer 中 `channel.basicAck/Nack` |
| prefetch | `spring.rabbitmq.listener.simple.prefetch: 10` |
| 死信队列 DLX | `DeadLetterRabbitConfig` + `POST /demo/dlx` |
| 消费幂等 | `tb_message_log` + `MessageLogRepository` |
| Quorum Queue | 各 `QueueBuilder.quorum()` |

## 端口说明

| 服务 | 宿主机端口 | 说明 |
| --- | --- | --- |
| MySQL | **3321** | 映射容器内 3306 |
| RabbitMQ AMQP | **5673** | 映射容器内 5672 |
| RabbitMQ 管理台 | **15673** | 账号 `admin` / `admin123` |
| 应用 | **8194** | Spring Boot |

管理台地址：`http://localhost:15673`

## 一键启动（Docker）

```bash
cd RabbitMQ
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose up --build
```

## 本地 Maven 构建

```bash
cd RabbitMQ
mvn clean package
java -jar target/rabbitmq-practice-1.0.0-SNAPSHOT.jar
```

本地开发需先启动 MySQL 和 RabbitMQ（可用 docker compose 只起中间件）：

```bash
docker compose up mysql rabbitmq -d
mvn spring-boot:run
```

## 接口示例

```bash
# Direct：创建订单 -> order.created.queue
curl -X POST "http://localhost:8194/orders" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":88.50}'

# Fanout：用户注册 -> sms.notice.queue + email.notice.queue
curl -X POST "http://localhost:8194/users/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"王五","email":"wangwu@example.com","phone":"13800000005"}'

# Topic：业务事件（order.paid 会进入 order.event.queue 和 all.event.queue）
curl -X POST "http://localhost:8194/orders/1/pay"

curl -X POST "http://localhost:8194/events/business" \
  -H "Content-Type: application/json" \
  -d '{"routingKey":"order.paid","content":"手动发送 Topic 事件"}'

# 死信演示（需设置 app.dlx.always-fail=true 后重启）
curl -X POST "http://localhost:8194/demo/dlx?userId=1&amount=9.90"

# 路由失败演示（触发 publisher-returns）
curl -X POST "http://localhost:8194/demo/invalid-route"

# 查看消费幂等日志
curl "http://localhost:8194/messages/logs?limit=20"
```

## 死信演示说明

在 `application.yml` 或环境变量中设置：

```yaml
app:
  dlx:
    always-fail: true
```

然后调用 `POST /demo/dlx`，正常消费者会 `basicNack(requeue=false)`，消息进入 `order.dead.queue`。

## 消息流转示意

```text
POST /orders
  -> 写 MySQL tb_order
  -> Direct: order.exchange [order.created]
  -> order.created.queue
  -> OrderCreatedConsumer（幂等 + manual ACK）

POST /users/register
  -> Fanout: user.registered.exchange
  -> sms.notice.queue / email.notice.queue

POST /orders/{id}/pay
  -> Topic: business.event.exchange [order.paid]
  -> order.event.queue (binding: order.#)
  -> all.event.queue (binding: #)
```
