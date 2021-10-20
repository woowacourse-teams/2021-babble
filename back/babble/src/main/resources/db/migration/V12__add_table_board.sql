create table board
(
    id         bigint auto_increment,
    title      varchar(255) not null,
    content    varchar(8000),
    view_count bigint,
    like_count bigint,
    nickname   varchar(255),
    password   varchar(255),
    category   varchar(255)  not null,
    created_at  timestamp,
    updated_at  timestamp,
    deleted boolean      not null,

    primary key (id)
);
