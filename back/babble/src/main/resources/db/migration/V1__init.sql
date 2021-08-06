create table game
(
    id    bigint auto_increment,
    image varchar(255) not null,
    name  varchar(255) not null,
    primary key (id)
);
create table room
(
    id            bigint auto_increment,
    created_date  timestamp,
    is_deleted    boolean not null,
    max_headcount integer,
    game_id       bigint  not null,
    primary key (id)
);
create table session
(
    id         bigint auto_increment,
    session_id varchar(255) not null,
    room_id    bigint       not null,
    user_id    bigint       not null,
    primary key (id)
);
create table tag
(
    id   bigint auto_increment,
    name varchar(255) not null,
    primary key (id)
);
create table tag_registration
(
    id       bigint auto_increment,
    room_id  bigint not null,
    tag_name bigint not null,
    primary key (id)
);
create table user
(
    id        bigint auto_increment,
    avatar    varchar(255) not null,
    joined_at timestamp,
    nickname  varchar(255) not null,
    room_id   bigint,
    primary key (id)
);
alter table room
    add constraint fk_room_game foreign key (game_id) references game(id);
alter table session
    add constraint fk_session_room foreign key (room_id) references room(id);
alter table session
    add constraint fk_session_user foreign key (user_id) references user(id);
alter table tag_registration
    add constraint fk_tag_registration_room foreign key (room_id) references room(id);
alter table tag_registration
    add constraint fk_tag_registration_tag foreign key (tag_name) references tag(id);
alter table user
    add constraint fk_user_room foreign key (room_id) references room(id);