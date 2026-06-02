SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP DATABASE IF EXISTS mp_demo;
CREATE DATABASE mp_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE mp_demo;

DROP TABLE IF EXISTS sys_order;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  deleted TINYINT NOT NULL DEFAULT 0,
  version INT NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL,
  update_time DATETIME NULL,
  UNIQUE KEY uk_sys_user_email (email)
);

CREATE TABLE sys_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  create_time DATETIME NOT NULL,
  KEY idx_sys_order_user_id (user_id)
);

INSERT INTO sys_user (username, email, age, status, deleted, version, create_time, update_time) VALUES
('张三', 'zhangsan@example.com', 20, 'ACTIVE', 0, 0, '2026-01-01 10:00:00', NULL),
('李四', 'lisi@example.com', 25, 'ACTIVE', 0, 0, '2026-01-02 10:00:00', NULL),
('王五', 'wangwu@example.com', 17, 'DISABLED', 0, 0, '2026-01-03 10:00:00', NULL);

INSERT INTO sys_order (user_id, order_no, amount, status, create_time) VALUES
(1, 'ORD-20260602-0001', 99.50, 'PAID', '2026-06-02 09:00:00'),
(1, 'ORD-20260602-0002', 19.90, 'CREATED', '2026-06-02 09:30:00'),
(2, 'ORD-20260602-0003', 199.00, 'PAID', '2026-06-02 10:00:00');

