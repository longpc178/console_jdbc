create database student_management;
use student_management;

create table mark_board
(
    id           bigint auto_increment
        primary key,
    student_id   bigint                    null,
    subject_name varchar(250) charset utf8 null,
    mark         float                     null,
    mark_year    int                       null
);

create table student
(
    id            bigint auto_increment
        primary key,
    code          varchar(250) charset utf8 null,
    name          varchar(250) charset utf8 null,
    date_of_birth varchar(250)              null,
    class_code    varchar(250) charset utf8 null,
    avg_mark      float                     null
);