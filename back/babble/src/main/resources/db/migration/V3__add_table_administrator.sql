create table administrator
(
    id    bigint auto_increment,
    ip varchar(255) not null unique,
    name  varchar(255) not null,
    primary key (id)
);