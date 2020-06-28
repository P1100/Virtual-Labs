# The SQL code below was generated automatically by Spring, run once on a new MariaDb container
create schema teams collate latin1_swedish_ci;
USE teams;
create table course
(
    name      varchar(255) not null
        primary key,
    enabled   bit          not null,
    max       int          not null,
    min       int          not null,
    professor varchar(255) null
);

create table hibernate_sequence
(
    next_val bigint null
);

create table role
(
    name        varchar(255) not null
        primary key,
    description varchar(255) null
);

create table student
(
    id         varchar(255) not null
        primary key,
    first_name varchar(255) null,
    name       varchar(255) null
);

create table student_course
(
    student_id  varchar(255) not null,
    course_name varchar(255) not null,
    constraint FKfplmnau8umrux0cy6n01792qd
        foreign key (course_name) references course (name),
    constraint FKq7yw2wg9wlt2cnj480hcdn6dq
        foreign key (student_id) references student (id)
);

create table teacher
(
    id varchar(255) not null
        primary key
);

create table team
(
    id        bigint       not null
        primary key,
    name      varchar(255) null,
    status    int          not null,
    course_id varchar(255) null,
    constraint FKrdbahenwatuua698jkpnfufta
        foreign key (course_id) references course (name)
);

create table teams_students
(
    team_id    bigint       not null,
    student_id varchar(255) not null,
    constraint FKarki525tmxnuk67piv1k9ioes
        foreign key (student_id) references student (id),
    constraint FKsvr6sg7agtmy6you2e96gihwc
        foreign key (team_id) references team (id)
);

create table token
(
    id          varchar(255) not null
        primary key,
    expiry_date datetime     null,
    team_id     bigint       null,
    student_id  varchar(255) null,
    constraint FK1j9myk0fjdutkio0tf3mne3u9
        foreign key (student_id) references student (id)
);

create table user
(
    username                varchar(255) not null
        primary key,
    account_non_expired     bit          not null,
    account_non_locked      bit          not null,
    credentials_non_expired bit          not null,
    enabled                 bit          not null,
    password                varchar(255) null
);

create table users_roles
(
    username  varchar(255) not null,
    authority varchar(255) not null,
    constraint FK7bg096328bak930kijaur5qd7
        foreign key (authority) references role (name),
    constraint FKcb8cti77dleh7oatpahd6g9i3
        foreign key (username) references user (username)
);


INSERT INTO teams.student (id, first_name, name)
VALUES ('100', 'atta@example.com', 'Atta Shah');
INSERT INTO teams.student (id, first_name, name)
VALUES ('101', 'alex@example.com', 'Alex Jones');
INSERT INTO teams.student (id, first_name, name)
VALUES ('102', 'jovan@example.com', 'Jovan Lee');
INSERT INTO teams.student (id, first_name, name)
VALUES ('103', 'greg@example.com', 'Greg Hover');
INSERT INTO teams.student (id, first_name, name)
VALUES ('110', 'ten@example.com', 'Ten Hover');
INSERT INTO teams.student (id, first_name, name)
VALUES ('S1', 'FirstName', 'name');
INSERT INTO teams.student (id, first_name, name)
VALUES ('S100', 'S1@@example.com', 'Duplicates S1 Test');
INSERT INTO teams.student (id, first_name, name)
VALUES ('S2', 'FirstName', 'name');
INSERT INTO teams.student (id, first_name, name)
VALUES ('S3', 'FirstName', 'name');
INSERT INTO teams.student (id, first_name, name)
VALUES ('S33', 'FirstName', 'name');
INSERT INTO teams.student (id, first_name, name)
VALUES ('S44', 's', 's');
INSERT INTO teams.course (name, enabled, max, min, professor)
VALUES ('C0', true, 1000, 1, 'professor');
INSERT INTO teams.course (name, enabled, max, min, professor)
VALUES ('C33', true, 100, 1, 'malnati');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('S33', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('100', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('101', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('102', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('103', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('S100', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('110', 'C0');
INSERT INTO teams.student_course (student_id, course_name)
VALUES ('S44', 'C0');
INSERT INTO teams.hibernate_sequence (next_val)
VALUES (12);
INSERT INTO teams.role (name)
VALUES ('ROLE_ADMIN');
INSERT INTO teams.role (name)
VALUES ('ROLE_GUEST');
INSERT INTO teams.role (name)
VALUES ('ROLE_PROFESSOR');
INSERT INTO teams.role (name)
VALUES ('ROLE_STUDENT');
INSERT INTO teams.role (name)
VALUES ('ROLE_USER');
INSERT INTO teams.token (id, expiry_date, team_id, student_id)
VALUES ('0ca57818-04a7-4432-b8e6-5ccd4523e70c', '2020-05-21 21:23:15', 11, '101');
INSERT INTO teams.token (id, expiry_date, team_id, student_id)
VALUES ('c15a77d2-f3dc-4b7e-8120-bce2c24e8fe9', '2020-05-21 21:23:15', 11, 'S33');
INSERT INTO teams.token (id, expiry_date, team_id, student_id)
VALUES ('d736d037-b79c-4d4a-8ce0-998b4186d91f', '2020-05-21 21:23:15', 11, '100');
INSERT INTO teams.team (id, name, status, course_id)
VALUES (11, 'Team0', 0, 'C0');
INSERT INTO teams.teams_students (team_id, student_id)
VALUES (11, '100');
INSERT INTO teams.teams_students (team_id, student_id)
VALUES (11, '101');
INSERT INTO teams.teams_students (team_id, student_id)
VALUES (11, 'S33');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('admin', true, true, true, true, '$2a$10$qF/y9z2HGmrDYY9PRTNXVeCvXGrQ0R6iUg8ufPpSndtNJWGZPg8FC');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('guest', true, true, true, true, '$2a$10$iq7S45rrruqf8riqbit1Y.LHR1vWeEF5O6Ox7J5gtdFgg5iJyAHW2');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('malnati', true, true, true, true, '$2a$10$CHNFhsFq9WeRlCTGHPMY1.X8QgyWNpd7ZdHEaTQ9TiXIC2SQtgTKW');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('professor', true, true, true, true, '$2a$10$sC7Y8BagFXw0tIM54a8YGup0enUpJRe/m1VF/UJRHNUoOZh6d2uB6');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('S1', true, true, true, true, '$2a$10$WAc/q.j3vDfE3RF7NcA7kuAIlo2TLhj4n3O2QQB4ASw84d.s/XnIW');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('s2', true, true, true, true, '$2a$10$jzlie6zD.f98sktzDFpZj.riuR//lfaSmO.yTlDUaLD56sxpoET9.');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('servetti', true, true, true, true, '$2a$10$6Zo76b4UQoq.DLiWKAhN.uftoIri7qeehdkaqUS1uhFRJps/6DnlW');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('student', true, true, true, true, '$2a$10$6hdwxi6iwQ2Wh6tL9Ed38u2C7K6lW.zCUAPibN7gx4H9bMiU8CXcC');
INSERT INTO teams.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password)
VALUES ('user', true, true, true, true, '$2a$10$pCfdikug5QZXF8JEHxOrc.fMreQEtILYLOaZM6wNI4VR6jHCK69W2');
INSERT INTO teams.users_roles (username, authority)
VALUES ('malnati', 'ROLE_USER');
INSERT INTO teams.users_roles (username, authority)
VALUES ('malnati', 'ROLE_PROFESSOR');
INSERT INTO teams.users_roles (username, authority)
VALUES ('malnati', 'ROLE_GUEST');
INSERT INTO teams.users_roles (username, authority)
VALUES ('malnati', 'ROLE_ADMIN');
INSERT INTO teams.users_roles (username, authority)
VALUES ('servetti', 'ROLE_USER');
INSERT INTO teams.users_roles (username, authority)
VALUES ('servetti', 'ROLE_PROFESSOR');
INSERT INTO teams.users_roles (username, authority)
VALUES ('S1', 'ROLE_USER');
INSERT INTO teams.users_roles (username, authority)
VALUES ('S1', 'ROLE_STUDENT');
INSERT INTO teams.users_roles (username, authority)
VALUES ('s2', 'ROLE_USER');
INSERT INTO teams.users_roles (username, authority)
VALUES ('s2', 'ROLE_STUDENT');
INSERT INTO teams.users_roles (username, authority)
VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO teams.users_roles (username, authority)
VALUES ('user', 'ROLE_USER');
INSERT INTO teams.users_roles (username, authority)
VALUES ('guest', 'ROLE_GUEST');
INSERT INTO teams.users_roles (username, authority)
VALUES ('professor', 'ROLE_PROFESSOR');
INSERT INTO teams.users_roles (username, authority)
VALUES ('student', 'ROLE_STUDENT');

