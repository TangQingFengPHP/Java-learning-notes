DROP DATABASE IF EXISTS flex_demo;
CREATE DATABASE flex_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE flex_demo;

DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  deleted TINYINT NOT NULL DEFAULT 0,
  version INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  UNIQUE KEY uk_user_email (email)
);

DROP TABLE IF EXISTS tb_order;
CREATE TABLE tb_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  KEY idx_order_user_id (user_id)
);

INSERT INTO tb_user (username, email, age, status, deleted, version, created_at, updated_at) VALUES
('张三', 'zhangsan@example.com', 20, 'ACTIVE', 0, 0, '2026-01-01 10:00:00', NULL),
('李四', 'lisi@example.com', 25, 'ACTIVE', 0, 0, '2026-01-02 10:00:00', NULL),
('王五', 'wangwu@example.com', 17, 'DISABLED', 0, 0, '2026-01-03 10:00:00', NULL);

INSERT INTO tb_order (user_id, order_no, amount, status, created_at) VALUES
(1, 'ORD-20260601-0001', 99.50, 'PAID', '2026-06-01 09:00:00'),
(1, 'ORD-20260601-0002', 19.90, 'CREATED', '2026-06-01 09:30:00'),
(2, 'ORD-20260601-0003', 199.00, 'PAID', '2026-06-01 10:00:00');

