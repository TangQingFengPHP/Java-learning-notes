SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP DATABASE IF EXISTS webflux_demo;
CREATE DATABASE webflux_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE webflux_demo;

DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_user_email (email)
);

INSERT INTO tb_user (username, email, age, status, created_at) VALUES
('张三', 'zhangsan@example.com', 20, 'ACTIVE', '2026-01-01 10:00:00'),
('李四', 'lisi@example.com', 25, 'ACTIVE', '2026-01-02 10:00:00'),
('王五', 'wangwu@example.com', 17, 'DISABLED', '2026-01-03 10:00:00');
