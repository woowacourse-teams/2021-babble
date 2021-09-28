create table slider
(
    id            bigint auto_increment,
    url           varchar(255) not null,
    sorting_index integer      not null,
    primary key (id)
);
