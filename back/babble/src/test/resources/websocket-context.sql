set
FOREIGN_KEY_CHECKS = 0;

truncate table tag_registration;
truncate table room;
truncate table session;
truncate table game;
truncate table tag;
truncate table user;
truncate table game_images;
alter table game auto_increment = 1;

set
FOREIGN_KEY_CHECKS = 1;

insert into game (deleted, `name`)
values (false, 'game1');
insert into game_images(game_id, game_image)
values (1, '게임 이미지1');
insert into game_images(game_id, game_image)
values (1, '게임 이미지2');
insert into game_images(game_id, game_image)
values (1, '게임 이미지3');
insert into tag (`name`)
values ('tag1');
insert into user (avatar, nickname)
values ('abc', '와일더');
insert into user (avatar, nickname)
values ('abc', '루트');
insert into user (avatar, nickname)
values ('abc', '포츈');
insert into user (avatar, nickname)
values ('abc', '현구막');
insert into room (created_at, deleted, game_id, max_headcount)
values ('2021-08-11T20:07:09.198', false, (select id from game where name = 'game1' limit 1), 4);
insert into tag_registration (room_id, tag_id)
values ((select id from room limit 1),
       (select id from tag limit 1) );