--liquibase formatted sql

--changeset dechernyakov:1
create table avatar
(
    id         bigserial primary key,
    file_path  text,
    file_size  bigint,
    media_type text,
    student_id bigint,
    foreign key (student_id) REFERENCES student (id)
);
--changeset dechernyakov:2
ALTER TABLE avatar
    ADD COLUMN data bytea


