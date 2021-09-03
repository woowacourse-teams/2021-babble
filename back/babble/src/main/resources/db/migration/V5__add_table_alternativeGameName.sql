create table alternative_game_name
(
    id      bigint auto_increment,
    name    varchar(255) not null,
    game_id bigint       not null,
    primary key (id)
);

alter table alternative_game_name
    add constraint fk_alternative_game_name_game foreign key (game_id) references game(id);