alter table game drop image;

create table game_images
(
    game_id    bigint,
    game_image varchar(255) not null
);

alter table game_images
    add constraint fk_game_images_game foreign key (game_id) references game (id);