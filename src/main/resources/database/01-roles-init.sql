--liquibase formatted sql
--changeset wd:1

INSERT INTO role
VALUES ('ROLE_STUDENT'),
       ('ROLE_TEACHER'),
       ('ROLE_ADMIN');