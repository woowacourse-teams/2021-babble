set FOREIGN_KEY_CHECKS = 0;

truncate table tag_registration;
truncate table room;
truncate table session;
truncate table game;
truncate table tag;
truncate table user;

set FOREIGN_KEY_CHECKS = 1;

insert into game (deleted, `name`, image) values (false, 'game1', 'image');
insert into tag (`name`) values ('tag1');
insert into user (avatar, nickname) values ('abc', '와일더');
insert into user (avatar, nickname) values ('abc', '루트');
insert into user (avatar, nickname) values ('abc', '포츈');
insert into room (created_at, deleted, game_id, max_headcount) values ('2021-08-11T20:07:09.198', false, (select id from game where name = 'game1' limit 1), 4);
insert into tag_registration (room_id, tag_id) values ((select id from room limit 1), (select  id from tag limit 1));