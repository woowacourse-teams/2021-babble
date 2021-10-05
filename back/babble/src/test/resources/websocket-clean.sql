set FOREIGN_KEY_CHECKS = 0;

truncate table tag_registration;
truncate table room;
truncate table session;
truncate table game;
truncate table tag;
truncate table user;
truncate table game_images;

set FOREIGN_KEY_CHECKS = 1;