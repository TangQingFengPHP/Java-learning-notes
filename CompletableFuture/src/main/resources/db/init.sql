SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP DATABASE IF EXISTS completable_future_demo;
CREATE DATABASE completable_future_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE completable_future_demo;

DROP TABLE IF EXISTS tb_order;
DROP TABLE IF EXISTS tb_product;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  balance DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  UNIQUE KEY uk_user_email (email)
);

CREATE TABLE tb_product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  price DECIMAL(12, 2) NOT NULL,
  stock INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL
);

CREATE TABLE tb_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(12, 2) NOT NULL,
  total_amount DECIMAL(12, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  KEY idx_order_user (user_id)
);

INSERT INTO tb_user (username, email, age, status, balance, created_at, updated_at) VALUES
('张三', 'zhangsan@example.com', 20, 'ACTIVE', 88.60, '2026-01-01 10:00:00', NULL),
('李四', 'lisi@example.com', 25, 'ACTIVE', 1288.50, '2026-01-02 10:00:00', NULL),
('王五', 'wangwu@example.com', 17, 'DISABLED', 0.00, '2026-01-03 10:00:00', NULL);

INSERT INTO tb_product (name, price, stock, status, created_at) VALUES
('Java 并发编程', 59.90, 100, 'ON_SALE', '2026-01-01 10:00:00'),
('Spring Boot 实战', 79.00, 50, 'ON_SALE', '2026-01-02 10:00:00'),
('MySQL 性能优化', 69.00, 0, 'ON_SALE', '2026-01-03 10:00:00');

INSERT INTO tb_order (user_id, product_id, quantity, unit_price, total_amount, status, created_at) VALUES
(1, 1, 1, 59.90, 59.90, 'PAID', '2026-01-10 10:00:00'),
(1, 2, 1, 79.00, 79.00, 'PAID', '2026-01-11 10:00:00'),
(2, 1, 2, 59.90, 119.80, 'PAID', '2026-01-12 10:00:00');
