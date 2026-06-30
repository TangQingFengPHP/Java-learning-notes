DROP TABLE IF EXISTS tb_message_log;
DROP TABLE IF EXISTS tb_order;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE tb_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE tb_message_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  message_id VARCHAR(64) NOT NULL,
  consumer_name VARCHAR(100) NOT NULL,
  routing_key VARCHAR(100) NULL,
  payload_summary VARCHAR(500) NULL,
  created_at TIMESTAMP NOT NULL,
  UNIQUE (message_id, consumer_name)
);
