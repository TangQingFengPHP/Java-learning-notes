create table orders (
    id bigint primary key auto_increment,
    user_id bigint not null,
    order_no varchar(50) not null,
    amount decimal(10, 2) not null,
    status varchar(20) not null,
    created_at datetime not null,
    index idx_orders_user_id (user_id),
    constraint fk_orders_user_id foreign key (user_id) references users (id)
);
