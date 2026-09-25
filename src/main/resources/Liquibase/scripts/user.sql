--liquibase formatted sql

--changeset dechernyakov:1

CREATE INDEX student_name_index ON student (name);
CREATE INDEX faculty_name_color on faculty(name,color);