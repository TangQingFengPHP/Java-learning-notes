create table users (
    id bigint primary key auto_increment,
    username varchar(50) not null,
    email varchar(100) not null,
    age int not null,
    created_at datetime not null,
    constraint uk_users_email unique (email)
);
