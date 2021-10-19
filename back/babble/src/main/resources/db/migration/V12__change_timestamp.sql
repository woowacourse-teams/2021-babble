alter table room change created_at created_at timestamp(3);
alter table session change created_at created_at timestamp(3);
alter table session change deleted_at deleted_at timestamp(3);
