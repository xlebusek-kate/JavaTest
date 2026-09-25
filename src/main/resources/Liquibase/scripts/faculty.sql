--liquibase formatted sql

--changeset dechernyakov:1
CREATE TABLE faculty (
    id bigserial primary key ,
    name varchar(255),
    color varchar(255)
)