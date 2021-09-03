create table alternative_tag_name
(
    id      bigint auto_increment,
    name    varchar(255) not null,
    tag_id bigint       not null,
    primary key (id)
);

alter table alternative_tag_name
    add constraint fk_alternative_tag_name_game foreign key (tag_id) references tag(id);