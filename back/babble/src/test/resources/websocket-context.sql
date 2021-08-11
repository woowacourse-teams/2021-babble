insert into game (deleted, `name`, image) values (false, 'game1', 'image');
insert into tag (`name`) values ('tag1');
insert into user (avatar, nickname) values ('abc', '와일더');
insert into room (created_at, deleted, game_id, max_headcount) values ('2021-08-11T20:07:09.198', false, 1, 4);
-- insert into session (created_at, deleted, session_id, room_id, user_id) values ('2021-08-11T20:07:09.198', false , '4444', 1, 1)
insert into tag_registration (room_id, tag_id) values (1, 1)