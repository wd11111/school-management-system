INSERT INTO role
VALUES ('ROLE_TEACHER'),
       ('ROLE_STUDENT');

INSERT INTO app_user
VALUES ('email', 'password', 'token'),
       ('email2', 'password', 'token'),
       ('email3', 'password', 'token'),
       ('email4', 'password', 'token');

INSERT INTO app_user_roles
VALUES ('email', 'ROLE_STUDENT'),
       ('email2', 'ROLE_TEACHER'),
       ('email3', 'ROLE_STUDENT'),
       ('email4', 'ROLE_TEACHER');

INSERT INTO school_class
VALUES ('1a'),
       ('1b');

INSERT INTO student
VALUES (1, 'studentName1', '1a', 'studentSurname1', 'email'),
       (2, 'studentName2', '1a', 'studentSurname2', 'email3');

INSERT INTO school_subject
VALUES ('biology'),
       ('history');

INSERT INTO mark
VALUES (1, 4, 1, 'biology'),
       (2, 1, 1, 'biology'),
       (3, 2, 1, 'history'),
       (4, 2, 2, 'history'),
       (5, 2, 2, 'history'),
       (6, 2, 2, 'history');

INSERT INTO teacher
VALUES (1, 'teacherName1', 'teacherSurname1', 'email2'),
       (2, 'teacherName1', 'teacherSurname2', 'email4');

INSERT INTO teacher_taught_subjects
VALUES (1, 'biology'),
       (1, 'history');

INSERT INTO teacher_in_class
VALUES (1, 'biology', 1),
       (2, 'history', 1);

INSERT INTO teacher_in_class_taught_classes
VALUES (1, '1a'),
       (2, '1a');

