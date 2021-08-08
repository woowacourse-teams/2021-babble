alter table game add deleted boolean not null;

alter table room change is_deleted deleted;

alter table tag add deleted boolean not null;

alter table tag_registration add deleted boolean not null;

alter table user add deleted boolean not null;
