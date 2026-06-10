SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP DATABASE IF EXISTS native_jdbc_demo;
CREATE DATABASE native_jdbc_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE native_jdbc_demo;

DROP TABLE IF EXISTS operation_log;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_users_email (email)
);

CREATE TABLE operation_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  content VARCHAR(200) NOT NULL,
  created_at DATETIME NOT NULL,
  KEY idx_operation_log_user_id (user_id)
);

CREATE TABLE orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  KEY idx_orders_user_id (user_id)
);

CREATE TABLE accounts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  owner_name VARCHAR(50) NOT NULL,
  balance DECIMAL(12, 2) NOT NULL,
  updated_at DATETIME NOT NULL
);

INSERT INTO users (username, email, age, status, created_at) VALUES
('张三', 'zhangsan@example.com', 20, 'ACTIVE', '2026-01-01 10:00:00'),
('李四', 'lisi@example.com', 25, 'ACTIVE', '2026-01-02 10:00:00'),
('王五', 'wangwu@example.com', 17, 'DISABLED', '2026-01-03 10:00:00');

INSERT INTO orders (user_id, order_no, amount, status, created_at) VALUES
(1, 'A001', 99.00, 'PAID', '2026-02-01 10:00:00'),
(1, 'A002', 260.00, 'PAID', '2026-02-02 10:00:00'),
(2, 'A003', 35.50, 'CANCELLED', '2026-02-03 10:00:00');

INSERT INTO accounts (owner_name, balance, updated_at) VALUES
('张三', 1000.00, '2026-01-01 10:00:00'),
('李四', 500.00, '2026-01-01 10:00:00');
