--liquibase formatted sql
--changeset wd:2

INSERT INTO app_user
VALUES ('Adam@', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123');

INSERT INTO app_user_roles
VALUES ('Adam@', 'ROLE_TEACHER'),
        ('Adam@', 'ROLE_ADMIN');

INSERT INTO teacher
values (1, 'Adam', 'Nowak', 'Adam@');