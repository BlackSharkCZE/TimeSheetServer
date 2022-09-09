-- liquibase formatted sql

--changeset jiri:1611904869675-1
alter table timeline add start_work_date date;
--rollback alter table timeline drop column start_work_date;

--changeset jiri:1611904869675-2
update timeline set start_work_date = cast(from_time as date);
--rollback update timeline set start_work_date = null;


