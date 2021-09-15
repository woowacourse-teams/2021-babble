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