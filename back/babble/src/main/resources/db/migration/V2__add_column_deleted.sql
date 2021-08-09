alter table game add deleted boolean not null;

alter table room change is_deleted deleted boolean not null;