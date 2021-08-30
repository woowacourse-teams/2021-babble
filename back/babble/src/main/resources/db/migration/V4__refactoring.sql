alter table room change created_date created_at timestamp;
alter table tag_registration change tag_name tag_id bigint not null;

alter table session add created_at timestamp;
alter table session add deleted_at timestamp null;
alter table session add deleted boolean not null default false;

alter table user drop column joined_at;