INSERT INTO role
VALUES ('ROLE_TEACHER'),
       ('ROLE_STUDENT');

INSERT INTO app_user
VALUES (1, 'jan.kowalski.1@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (2, 'joanna.nowak.2@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (3, 'tomasz.wiśniewski.3@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (4, 'anna.wójcik.4@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (5, 'piotr.kowalczyk.5@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (6, 'magdalena.lewandowska.6@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK',
        '123'),
       (7, 'marcin.zieliński.7@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (8, 'agnieszka.szymańska.8@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (9, 'michał.dąbrowski.9@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (10, 'monika.kozłowska.10@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (11, 'teacher1@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123'),
       (12, 'teacher2@example.com', '$2a$10$7IqOSL/QkiC8i.i.uTcAFOLBnGm51BKOIava3A6SRV16JbS5LtXDK', '123');

INSERT INTO app_user_roles
VALUES (1, 'ROLE_STUDENT'),
    (2, 'ROLE_STUDENT'),
    (3, 'ROLE_STUDENT'),
    (4, 'ROLE_STUDENT'),
    (5, 'ROLE_STUDENT'),
    (6, 'ROLE_STUDENT'),
    (7, 'ROLE_STUDENT'),
    (8, 'ROLE_STUDENT'),
    (9, 'ROLE_STUDENT'),
    (10, 'ROLE_STUDENT'),
    (11, 'ROLE_TEACHER'),
    (12, 'ROLE_TEACHER');

INSERT INTO school_class
VALUES ('1A'),
       ('1B');

INSERT INTO student (id ,name, surname, birth_Date, school_class, app_user_id)
VALUES (1,'Jan', 'Kowalski', '2000-01-01', '1A', 1),
       (2,'Joanna', 'Nowak', '2000-02-01', '1A', 2),
       (3,'Tomasz', 'Wiśniewski', '1999-12-31', '1A', 3),
       (4,'Anna', 'Wójcik', '2000-03-01', '1A', 4),
       (5,'Piotr', 'Kowalczyk', '1999-11-30', '1A', 5),
       (6,'Magdalena', 'Lewandowska', '1999-10-31', '1B', 6),
       (7,'Marcin', 'Zieliński', '1999-09-30', '1B', 7),
       (8,'Agnieszka', 'Szymańska', '1999-08-31', '1B', 8),
       (9,'Michał', 'Dąbrowski', '1999-07-31', '1B', 9),
       (10,'Monika', 'Kozłowska', '1999-06-30', '1B', 10);

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
VALUES (1, 'teacherName1', 'teacherSurname1', 11),
       (2, 'teacherName1', 'teacherSurname2', 12);

INSERT INTO teacher_taught_subjects
VALUES (1, 'biology'),
       (1, 'history');

INSERT INTO teacher_in_class
VALUES (1, 'biology', 1),
       (2, 'history', 1);

INSERT INTO teacher_in_class_taught_classes
VALUES (1, '1A'),
       (2, '1A');

