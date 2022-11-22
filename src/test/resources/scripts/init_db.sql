INSERT INTO role
VALUES ('ROLE');

INSERT INTO app_user
VALUES ('email', 'password', 'token');

INSERT INTO app_user_roles
VALUES ('email', 'ROLE');

INSERT INTO school_class
VALUES ('1a');

INSERT INTO student
VALUES (3, 'name', '1a', 'surname', 'email');

INSERT INTO school_subject
VALUES ('biology'),
       ('history');

INSERT INTO mark
VALUES (1, 4, 3, 'biology'),
       (2, 1, 3, 'biology'),
       (3, 2, 3, 'history');
