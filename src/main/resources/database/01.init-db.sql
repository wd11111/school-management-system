--liquibase formatted sql
--changeset wd:1

create table "app_user"
(
    "user_email" varchar(255) not null,
    "password"   varchar(255),
    "token"      varchar(255),
    primary key ("user_email")
);
create table "app_user_roles"
(
    "app_user_user_email" varchar(255) not null,
    "roles_role"          varchar(255) not null
);
create table "mark"
(
    "id"         bigserial not null,
    "mark"       int2      not null,
    "student_id" int8      not null,
    "subject"    varchar(255) not null,
    primary key ("id")
);
create table "role"
(
    "role" varchar(255) not null,
    primary key ("role")
);
create table "school_class"
(
    "name" varchar(255) not null,
    primary key ("name")
);
create table "school_subject"
(
    "name" varchar(255) not null,
    primary key ("name")
);
create table "student"
(
    "id"           bigserial not null,
    "name"         varchar(255),
    "school_class" varchar(255),
    "surname"      varchar(255),
    "user_email"   varchar(255),
    primary key ("id")
);
create table "teacher"
(
    "id"         bigserial not null,
    "name"       varchar(255),
    "surname"    varchar(255),
    "user_email" varchar(255),
    primary key ("id")
);
create table "teacher_in_class"
(
    "id"             bigserial not null,
    "taught_subject" varchar(255),
    "teacher_id"     int8,
    primary key ("id")
);
create table "teacher_in_class_taught_classes"
(
    "teachers_in_class_id" int8         not null,
    "taught_classes_name"  varchar(255) not null,
    primary key ("teachers_in_class_id", "taught_classes_name")
);
create table "teacher_taught_subjects"
(
    "teachers_id"          int8         not null,
    "taught_subjects_name" varchar(255) not null,
    primary key ("teachers_id", "taught_subjects_name")
);
alter table if exists "app_user_roles"
    add constraint "FK_roles_role_role" foreign key ("roles_role") references "role";
alter table if exists "app_user_roles"
    add constraint "FK_app_user_user_email_app_user" foreign key ("app_user_user_email") references "app_user";
alter table if exists "mark"
    add constraint "FK_subject_school_subject" foreign key ("subject") references "school_subject";
alter table if exists "student"
    add constraint "FK_user_email_app_user" foreign key ("user_email") references "app_user";
alter table if exists "student"
    add constraint "FK_school_class_school_class" foreign key ("school_class") references "school_class";
alter table if exists "teacher"
    add constraint "FK_user_email_app_user" foreign key ("user_email") references "app_user";
alter table if exists "teacher_in_class"
    add constraint "FK_teacher_id_teacher" foreign key ("teacher_id") references "teacher";
alter table if exists "teacher_in_class"
    add constraint "FK_taught_subject_school_subject" foreign key ("taught_subject") references "school_subject";
alter table if exists "teacher_in_class_taught_classes"
    add constraint "FK_taught_classes_name_school_class" foreign key ("taught_classes_name") references "school_class";
alter table if exists "teacher_in_class_taught_classes"
    add constraint "FK_teachers_in_class_id_teacher_in_class" foreign key ("teachers_in_class_id") references "teacher_in_class";
alter table if exists "teacher_taught_subjects"
    add constraint "FK_taught_subjects_name_school_subject" foreign key ("taught_subjects_name") references "school_subject";
alter table if exists "teacher_taught_subjects"
    add constraint "FK_teachers_id_teacher" foreign key ("teachers_id") references "teacher"
