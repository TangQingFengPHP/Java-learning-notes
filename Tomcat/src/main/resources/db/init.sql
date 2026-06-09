    SET NAMES utf8mb4;
    SET CHARACTER SET utf8mb4;

    DROP DATABASE IF EXISTS tomcat_demo;
    CREATE DATABASE tomcat_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
    USE tomcat_demo;

    DROP TABLE IF EXISTS orders;
    DROP TABLE IF EXISTS users;

    CREATE TABLE users (
      id BIGINT PRIMARY KEY AUTO_INCREMENT,
      username VARCHAR(50) NOT NULL,
      email VARCHAR(100) NOT NULL,
      age INT NOT NULL,
      status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
      created_at DATETIME NOT NULL,
      UNIQUE KEY uk_users_email (email)
    );

    CREATE TABLE orders (
      id BIGINT PRIMARY KEY AUTO_INCREMENT,
      user_id BIGINT NOT NULL,
      order_no VARCHAR(50) NOT NULL,
      amount DECIMAL(10, 2) NOT NULL,
      status VARCHAR(20) NOT NULL,
      created_at DATETIME NOT NULL,
      INDEX idx_orders_user_id (user_id),
      CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
    );

    INSERT INTO users (username, email, age, status, created_at) VALUES
    ('张三', 'zhangsan@example.com', 20, 'ACTIVE', '2026-01-01 10:00:00'),
    ('李四', 'lisi@example.com', 25, 'ACTIVE', '2026-01-02 10:00:00'),
    ('王五', 'wangwu@example.com', 17, 'DISABLED', '2026-01-03 10:00:00');

    INSERT INTO orders (user_id, order_no, amount, status, created_at) VALUES
    (1, 'A001', 99.00, 'PAID', '2026-02-01 10:00:00'),
    (1, 'A002', 260.00, 'PAID', '2026-02-02 10:00:00'),
    (2, 'A003', 35.50, 'CANCELLED', '2026-02-03 10:00:00');
