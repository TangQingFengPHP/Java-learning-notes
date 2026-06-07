insert into users (username, email, age, status, created_at)
values
    ('张三', 'zhangsan@example.com', 20, 'ACTIVE', '2026-01-01 10:00:00'),
    ('李四', 'lisi@example.com', 25, 'ACTIVE', '2026-01-02 10:00:00'),
    ('王五', 'wangwu@example.com', 17, 'ACTIVE', '2026-01-03 10:00:00');

insert into orders (user_id, order_no, amount, status, created_at)
values
    (1, 'A001', 99.00, 'PAID', '2026-02-01 10:00:00'),
    (1, 'A002', 260.00, 'PAID', '2026-02-02 10:00:00'),
    (2, 'A003', 35.50, 'CANCELLED', '2026-02-03 10:00:00');

update users
set status = 'DISABLED'
where email = 'wangwu@example.com';
