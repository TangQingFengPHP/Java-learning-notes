create table app_config (
    id bigint primary key auto_increment,
    config_key varchar(100) not null,
    config_value varchar(500) not null,
    constraint uk_app_config_key unique (config_key)
);

insert into app_config (config_key, config_value)
values ('app.name', 'flyway-practice');
