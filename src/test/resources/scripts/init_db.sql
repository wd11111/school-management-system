INSERT INTO role
VALUES ('ROLE');

INSERT INTO app_user
VALUES ('email', 'password', 'token'),
       ('email2', 'password', 'token');

INSERT INTO app_user_roles
VALUES ('email', 'ROLE');

INSERT INTO school_class
VALUES ('1a'),
       ('1b');

INSERT INTO student
VALUES (3, 'name', '1a', 'surname', 'email');

INSERT INTO school_subject
VALUES ('biology'),
       ('history');

INSERT INTO mark
VALUES (1, 4, 3, 'biology'),
       (2, 1, 3, 'biology'),
       (3, 2, 3, 'history');

INSERT INTO teacher
VALUES (1, 'name', 'surname', 'email2');

INSERT INTO teacher_taught_subjects
VALUES (1, 'biology'),
       (1, 'history');

INSERT INTO teacher_in_class
VALUES (1, 'biology', 1),
        (2, 'history', 1);

INSERT INTO teacher_in_class_taught_classes
VALUES (1, '1a'),
       (2, '1a');

