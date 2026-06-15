DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NULL,
  UNIQUE (email)
);

INSERT INTO tb_user (username, email, age, status, created_at, updated_at) VALUES
('张三', 'zhangsan@example.com', 20, 'ACTIVE', '2026-01-01 10:00:00', NULL),
('李四', 'lisi@example.com', 25, 'ACTIVE', '2026-01-02 10:00:00', NULL),
('王五', 'wangwu@example.com', 17, 'DISABLED', '2026-01-03 10:00:00', NULL);
