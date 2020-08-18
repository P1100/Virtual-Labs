# create DATABASE virtuallabstest;

create schema virtuallabs collate latin1_swedish_ci;
USE virtuallabs;

create table course
(
    id           varchar(255) not null
        primary key,
    enabled      bit          not null,
    full_name    varchar(255) null,
    max_enrolled int          not null,
    min_enrolled int          not null
);

create table hibernate_sequence
(
    next_val bigint null
);

create table student
(
    id            varchar(255) not null
        primary key,
    email         varchar(255) null,
    first_name    varchar(255) null,
    last_name     varchar(255) null,
    profile_photo varchar(255) null
);

create table student_course
(
    student_id varchar(255) not null,
    course_id  varchar(255) not null,
    constraint FKejrkh4gv8iqgmspsanaji90ws
        foreign key (course_id) references course (id),
    constraint FKq7yw2wg9wlt2cnj480hcdn6dq
        foreign key (student_id) references student (id)
);

create table teacher
(
    id            varchar(255) not null
        primary key,
    email         varchar(255) null,
    first_name    varchar(255) null,
    last_name     varchar(255) null,
    profile_photo varchar(255) null
);

create table teacher_course
(
    teacher_id varchar(255) not null,
    course_id  varchar(255) not null,
    constraint FKaleldsg7yww5as540ld8iwghe
        foreign key (teacher_id) references teacher (id),
    constraint FKp8bco6842vkqh13y4759ib7tk
        foreign key (course_id) references course (id)
);

create table team
(
    id                        bigint       not null
        primary key,
    max_disk_space            bigint       null,
    max_ram                   bigint       null,
    max_vcpu                  bigint       null,
    max_vm_istances_available bigint       null,
    max_vm_istances_enabled   bigint       null,
    name                      varchar(255) null,
    status                    int          not null,
    course_id                 varchar(255) null,
    constraint FKrdbahenwatuua698jkpnfufta
        foreign key (course_id) references course (id)
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



INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('1', true, 'Applicazioni Internet', 300, 1);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('2', true, 'Mobile Development', 200, 1);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('3', true, 'Programmazione di sistema', 10000, 10);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('4', true, 'Software Engineering I', 10, 1);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('5', true, 'Software Engineering II', 1, 100);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('6', false, 'Disabled Course', 1, 100);
INSERT INTO virtuallabs.course (id, enabled, full_name, max_enrolled, min_enrolled)
VALUES ('7', true, 'Min Max 2 3', 2, 3);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('10', 'lm@polito.it', 'Luca', 'Manni', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('11', 'vg@polito.it', 'Valentina', 'Guano', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('2', 'pf@polito.it', 'Paolo', 'Ferri', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('3', 'ge@polito.it', 'Gennaro', 'Esposito', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('4', 'am@polito.it', 'Alberto', 'Muti', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('556753', 'gr@polito.it', 'Gino', 'Rossi', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('667567', 'gb@polito.it', 'Giovanni', 'Bianchi', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('7', 'tt@polito.it', 'Tizio', 'Tizio', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('8', 'cc@polito.it', 'Caio', 'Caio', null);
INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo)
VALUES ('9', 'at@polito.it', 'Alberto', 'Terri', null);
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('8', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('667567', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('556753', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('2', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('2', '7');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('2', '5');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('2', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('3', '7');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('3', '5');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('3', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('4', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('4', '2');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('4', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('9', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('9', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '2');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '3');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '5');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('10', '7');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('11', '3');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('11', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('11', '7');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('1', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('1', '3');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('1', '5');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('1', '4');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('1', '7');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('7', '1');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('7', '3');
INSERT INTO virtuallabs.student_course (student_id, course_id)
VALUES ('7', '7');
INSERT INTO virtuallabs.teacher (id, email, first_name, last_name, profile_photo)
VALUES ('1', 'gm@polito.it', 'G', 'Malnati', null);
INSERT INTO virtuallabs.teacher (id, email, first_name, last_name, profile_photo)
VALUES ('2', 'as@polito.it', 'A', 'Servetti', null);
INSERT INTO virtuallabs.teacher (id, email, first_name, last_name, profile_photo)
VALUES ('3', 'uu@polito.it', 'U', 'Unknown', null);
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('1', '1');
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('1', '2');
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('2', '1');
INSERT INTO virtuallabs.teacher_course (teacher_id, course_id)
VALUES ('3', '4');
# INSERT INTO virtuallabs.hibernate_sequence (next_val) VALUES (1);INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_ADMIN', null);
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('admin', true, true, true, true, '$2a$10$qF/y9z2HGmrDYY9PRTNXVeCvXGrQ0R6iUg8ufPpSndtNJWGZPg8FC');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('guest', true, true, true, true, '$2a$10$iq7S45rrruqf8riqbit1Y.LHR1vWeEF5O6Ox7J5gtdFgg5iJyAHW2');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('malnati', true, true, true, true, '$2a$10$CHNFhsFq9WeRlCTGHPMY1.X8QgyWNpd7ZdHEaTQ9TiXIC2SQtgTKW');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('professor', true, true, true, true, '$2a$10$sC7Y8BagFXw0tIM54a8YGup0enUpJRe/m1VF/UJRHNUoOZh6d2uB6');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('S1', true, true, true, true, '$2a$10$WAc/q.j3vDfE3RF7NcA7kuAIlo2TLhj4n3O2QQB4ASw84d.s/XnIW');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('s2', true, true, true, true, '$2a$10$jzlie6zD.f98sktzDFpZj.riuR//lfaSmO.yTlDUaLD56sxpoET9.');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('servetti', true, true, true, true, '$2a$10$6Zo76b4UQoq.DLiWKAhN.uftoIri7qeehdkaqUS1uhFRJps/6DnlW');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('student', true, true, true, true, '$2a$10$6hdwxi6iwQ2Wh6tL9Ed38u2C7K6lW.zCUAPibN7gx4H9bMiU8CXcC');
# INSERT INTO virtuallabs.user (username, account_non_expired, account_non_locked, credentials_non_expired, enabled, password) VALUES ('user', true, true, true, true, '$2a$10$pCfdikug5QZXF8JEHxOrc.fMreQEtILYLOaZM6wNI4VR6jHCK69W2');INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('malnati', 'ROLE_USER');
# INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_GUEST', null);
# INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_PROFESSOR', null);
# INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_STUDENT', null);
# INSERT INTO virtuallabs.role (name, description) VALUES ('ROLE_USER', null);
# INSERT INTO virtuallabs.student (id, email, first_name, last_name, profile_photo) VALUES ('1', 'pg@polito.it', 'Pietro', 'Giasone', null);
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('malnati', 'ROLE_PROFESSOR');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('malnati', 'ROLE_GUEST');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('malnati', 'ROLE_ADMIN');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('servetti', 'ROLE_USER');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('servetti', 'ROLE_PROFESSOR');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('S1', 'ROLE_USER');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('S1', 'ROLE_STUDENT');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('s2', 'ROLE_USER');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('s2', 'ROLE_STUDENT');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('admin', 'ROLE_ADMIN');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('user', 'ROLE_USER');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('guest', 'ROLE_GUEST');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('professor', 'ROLE_PROFESSOR');
# INSERT INTO virtuallabs.users_roles (username, authority) VALUES ('student', 'ROLE_STUDENT');
