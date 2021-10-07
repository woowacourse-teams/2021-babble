alter table administrator add deleted boolean not null default false;
alter table tag_registration add deleted boolean not null default false;
alter table user add deleted boolean not null default false;
