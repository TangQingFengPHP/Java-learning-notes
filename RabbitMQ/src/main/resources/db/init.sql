SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP DATABASE IF EXISTS rabbit_demo;
CREATE DATABASE rabbit_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE rabbit_demo;

DROP TABLE IF EXISTS tb_message_log;
DROP TABLE IF EXISTS tb_order;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_user_email (email)
);

CREATE TABLE tb_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_order_user_id (user_id)
);

CREATE TABLE tb_message_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  message_id VARCHAR(64) NOT NULL,
  consumer_name VARCHAR(100) NOT NULL,
  routing_key VARCHAR(100) NULL,
  payload_summary VARCHAR(500) NULL,
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_message_consumer (message_id, consumer_name)
);

INSERT INTO tb_user (username, email, phone, created_at) VALUES
('张三', 'zhangsan@example.com', '13800000001', '2026-01-01 10:00:00'),
('李四', 'lisi@example.com', '13800000002', '2026-01-02 10:00:00');

INSERT INTO tb_order (order_no, user_id, amount, status, created_at) VALUES
('ORD-20260601-0001', 1, 99.50, 'CREATED', '2026-06-01 09:00:00');
