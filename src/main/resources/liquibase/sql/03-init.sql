INSERT INTO app_user
VALUES (19, 'adam123@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123');

INSERT INTO app_user_roles
VALUES (19, 'ROLE_TEACHER'),
       (19, 'ROLE_ADMIN');

INSERT INTO teacher
values (1, 'Adam', 'Nowak', 19);