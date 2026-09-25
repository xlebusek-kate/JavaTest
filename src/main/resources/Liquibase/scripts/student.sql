--liquibase formatted sql

--changeset dechernyakov:1
CREATE
    TABLE student
(
    id   bigserial primary key,
    name varchar(255),
    age  int check (age > 0),
    faculty_id bigint,
    foreign key (faculty_id) REFERENCES faculty(id)
)