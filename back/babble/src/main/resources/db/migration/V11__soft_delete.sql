alter table administrator add deleted boolean not null default false;
alter table tag_registration add deleted boolean not null default false;
alter table user add deleted boolean not null default false;

alter table alternative_game_name change is_deleted deleted boolean not null default false;
alter table alternative_tag_name change is_deleted deleted boolean not null default false;
